/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.Partner;
import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.services.UserService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import de.contentcreation.pplive.model.UserBean;
import de.contentcreation.pplive.services.DatabaseHandler;
import de.contentcreation.pplive.util.QueryHelper;
import de.contentcreation.pplive.websockets.BacklogClientEndpoint;
import de.contentcreation.pplive.websockets.BacklogClientEndpoint.BacklogArticleMessageHandler;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Diese Bean ist der Controller für die Login-Seite. Sie ist für die
 * Entgegennahme der eingegebenen Daten und Abgleich mit der
 * dahintergeschalteten Stateless UserService zuständig. Wenn die Login-Daten
 * zutreffen, wird der Login zugelassen und eine Session erstellt, ansonsten
 * gibt es einen Fehlermeldung.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@RequestScoped
public class LoginBean implements Serializable {

    private List<Integer> partnerList;

    private List<Integer> selectedPartners;

    private List<Partner> partnerListNew;

    private List<Partner> partnerListTransposed;

    private List<Partner> selectedPartnersNew;

    private BacklogClientEndpoint backlogClient;

    private Boolean unknownPartnersFound;

    private List<Integer> unknownPartners;

    private String websocketChannel;

    @Inject
    private UserBean bean;

    @EJB
    private UserService service;

    @EJB
    private DatabaseHandler dbHandler;

    private String username;

    private String password;

    private BacklogClientEndpoint client;

    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = this.md5(password);
    }

    public List<Integer> getPartnerList() {
        return dbHandler.getPartner();
    }

    public void setPartnerList(List<Integer> partnerList) {
        this.partnerList = partnerList;
    }

//    public List<Partner> getPartnerListTransposed() {
//        if (partnerListTransposed == null) {
//            partnerListTransposed = loadTransposedList();
//        }
//        return partnerListTransposed;
//    }
    public void setPartnerListTransposed(List<Partner> partnerListTransposed) {
        this.partnerListTransposed = partnerListTransposed;
    }

    public List<Integer> getSelectedPartners() {
        return selectedPartners;
    }

    public void setSelectedPartners(List<Integer> selectedPartners) {
        this.selectedPartners = selectedPartners;
    }

    public List<Partner> getPartnerListNew() {
        if (partnerListNew == null) {
            partnerListNew = loadPartnerList();
        }
        return partnerListNew;
    }

    public void setPartnerListNew(List<Partner> partnerListNew) {
        this.partnerListNew = partnerListNew;
    }

    public List<Partner> getSelectedPartnersNew() {
        return selectedPartnersNew;
    }

    public void setSelectedPartnersNew(List<Partner> selectedPartnersNew) {
        this.selectedPartnersNew = selectedPartnersNew;
    }

    public List<Integer> getUnknownPartners() {
        if (unknownPartners == null) {
            unknownPartners = loadUnknownPartners();
        }
        return unknownPartners;
    }

    public void setUnknownPartners(List<Integer> unknownPartners) {
        this.unknownPartners = unknownPartners;
    }

    public Boolean getUnknownPartnersFound() {
        if (unknownPartnersFound == null) {
            unknownPartnersFound = loadUnknownPartnersFound();
        }
        return unknownPartnersFound;
    }

    public void setUnknownPartnersFound(Boolean unknownPartnersFound) {
        this.unknownPartnersFound = unknownPartnersFound;
    }

    /**
     * Diese Methode verschlüsselt das eingegebene Passwort in einen MD5-Hash
     *
     * @param input Das eingegebene Passwort
     * @return MD5-Hash als String
     */
    private String md5(String input) {
        String md5 = null;
        if (input == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(input.getBytes(), 0, input.length());

            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private List<Partner> loadPartnerList() {
        return dbHandler.getPartnerLoginScreen();
    }

    /**
     * Diese Methode führt den Abgleich der eingegebenen Daten auf der
     * Login-Seite mit in der DB hiexitnterlegen Daten durch, indem auf den
     * UserService zugegriffen wird und geschaut wird, ob ein Eintrag mit der
     * Kombination aus Nutzername + Passwort existiert.
     *
     * @param username Der eingegebene Nutzername.
     * @param password Das eingegebene Passwort.
     * @param partnerList Die gewählten Partner, die für eine Sitzung bearbeitet
     * werden sollen.
     */
    public String login(String username, String password, List<Integer> partnerList) {
        String direction = "";
        if (partnerList == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kein Partner ausgewählt", "Bitte mindestens einen Partner auswählen."));
        }
        User user = service.login(username, password);
        if (user != null && partnerList != null) {
            bean.setUser(user);
            bean.setValid(true);
            bean.setVorname(username);
            bean.setNachname(username);
            bean.setPartnerList(partnerList);
            websocketChannel = "ws://10.170.43.26:8080/Partnerprogramm/primepush/backlogChannel";
//            String pathParameter = QueryHelper.IntegerListToStringParameter(partnerList);
//            websocketChannel = websocketChannel.concat(pathParameter);
            try {
                bean.connectWebSocket(websocketChannel, partnerList);
            } catch (URISyntaxException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Willkommen."));
            direction = "backlogOverview.jsf?faces-redirect=true";
        } else if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Benutzername '" + username + "' oder Passwort ist nicht korrekt!"));
        }
        return direction;
    }

    public String redirectedLogin(String username, String password, List<Integer> partnerList) {
        String direction = "";
        if (partnerList == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kein Partner ausgewählt", "Bitte mindestens einen Partner auswählen."));
            direction = null;
        }
        User user = service.login(username, password);
        if (user != null && partnerList != null) {
            websocketChannel = "ws://localhost:8080/Partnerprogramm/primepush/backlogChannel";
            bean.setUser(user);
            bean.setValid(true);
            bean.setVorname(username);
            bean.setNachname(username);
            bean.setPartnerList(partnerList);
            try {
                bean.connectWebSocket(websocketChannel, partnerList);
            } catch (URISyntaxException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            direction = "backlogOverview.jsf?faces-redirect=true";
        } else if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Benutzername '" + username + "' oder Passwort ist nicht korrekt!"));
            direction = null;
        }
        return direction;
    }

    /**
     * Zerstört die Session, wenn sich ein Nutzer ausloggt.
     */
    public void resetUser() {
        if (bean != null) {

            // Bean zerstören
            bean.setNick(null);
            bean.setPasswort(null);
            bean.setValid(false);
            bean.setPartnerList(null);
            bean.setUser(null);
            bean = null;

            // Session zerstören
            HttpSession session = (HttpSession) FacesContext
                    .getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        }

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Logout war erfolgreich.");
        FacesContext.getCurrentInstance().addMessage(null, message);
//        return "login.jsf?faces-redirect = true";
    }

    public List<Partner> getTransposedList() {
        if (partnerListNew == null) {
            partnerListNew = loadPartnerList();
        }
//        System.out.println("this = " + this);
        int columns = partnerListNew.size() / 9 + 1;
        LinkedList<List<Partner>> listOfLists = splitToMulitpleLists(partnerListNew, columns);

//        System.out.println("partnerListNew = " + partnerListNew);
//        for (List<Partner> l : listOfLists) {
//            System.out.println(l);
//        }
        List<Partner> newList = createNewList(listOfLists, 9);
//        System.out.println("newList = " + newList.size());
        return newList;
    }

    private static LinkedList<Partner> createNewList(List<List<Partner>> listOfLists, int columns) {
        LinkedList<Partner> newList = new LinkedList<>();
        for (int i = 0; i < listOfLists.get(0).size(); i++) {
            for (List<Partner> l : listOfLists) {
                try {
                    System.out.println("l = " + l);
                    newList.add(l.get(i));
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    continue;
                }
            }
        }
        return newList;
    }

    private static LinkedList<List<Partner>> splitToMulitpleLists(List<Partner> list, int columnCount) {
        int listCount = list.size() / columnCount + 1;
        LinkedList<List<Partner>> listOfLists = new LinkedList<>();
        for (int i = 0; i < listCount; i++) {
            List<Partner> helperList = new LinkedList<>();
            listOfLists.add(helperList);
        }

        for (int i = 0; i < listOfLists.size(); i++) {
            for (int j = 0; j < columnCount; j++) {
                try {
                    listOfLists.get(i).add(list.get(columnCount * i + j));
                } catch (IndexOutOfBoundsException ex) {
                    break;
                }
            }
        }
//        System.out.println("listOfLists = " + listOfLists);
        return listOfLists;
    }

    private boolean loadUnknownPartnersFound() {
        return dbHandler.unknownPartnersFound();
    }

    private List<Integer> loadUnknownPartners() {
        return dbHandler.getUnknownPartners();
    }

}
