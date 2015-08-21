/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

    @Inject
    private UserBean bean;

    @EJB
    private UserService service;

    @EJB
    private DatabaseHandler dbHandler;

    private String username;

    private String password;

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

    public List<Partner> getPartnerListTransposed() {
        if (partnerListTransposed == null) {
            partnerListTransposed = loadTransposedList();
        }
        return partnerListTransposed;
    }

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
    public void login(String username, String password, List<Integer> partnerList) {
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Willkommen."));

        } else if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Benutzername '" + username + "' oder Passwort ist nicht korrekt!"));
        }
    }

    public String redirectedLogin(String username, String password, List<Integer> partnerList) {
        String direction = "";
        if (partnerList == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kein Partner ausgewählt", "Bitte mindestens einen Partner auswählen."));
            direction = null;
        }
        User user = service.login(username, password);
        if (user != null && partnerList != null) {
            bean.setUser(user);
            bean.setValid(true);
            bean.setVorname(username);
            bean.setNachname(username);
            bean.setPartnerList(partnerList);
            direction = "backlogOverview.jsf?faces-redirect=true";
        } else if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Benutzername '" + username + "' oder Passwort ist nicht korrekt!"));
            direction = null;
        }
        return direction;
    }

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

    private List<Partner> loadTransposedList() {
        if (partnerListNew == null) {
            partnerListNew = loadPartnerList();
        }

        int columns = partnerListNew.size() / 9 + 1;
//        System.out.println("partnerListNew = " + partnerListNew.size());
        int rows = loadPartnerList().size() / columns + 1;

        Partner[][] newArrayContent = listToMatrix(partnerListNew, columns);

        List<Partner> list = sanitizeList(flattenArray(transposeMatrix(newArrayContent)));       
        return list;

    }

    private static List<Partner> flattenArray(Partner[][] array) {
        List<Partner> list = new LinkedList<>();
        for (int i = 0; i < array.length; i++) {
            for (int x = 0; x < array[i].length; x++) {
                list.add(array[i][x]);
            }
        }
        return list;
    }

    private static Partner[][] transposeMatrix(Partner[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        Partner[][] trasposedMatrix = new Partner[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                trasposedMatrix[x][y] = matrix[y][x];
            }
        }

        return trasposedMatrix;
    }

    private static String matrixToString(Partner[][] a) {
        int m = a.length;
        int n = a[0].length;

        String tmp = "";

        for (int y = 0; y < m; y++) {
            for (int x = 0; x < n; x++) {
                tmp = tmp + a[y][x] + " ";
            }

            tmp = tmp + "\n";
        }

        return tmp;
    }

    private static Partner[][] listToMatrix(List<Partner> list, int columns) {

        int rows = list.size() / columns + 1;
        Partner[][] newArrayContent = new Partner[rows][columns];

        for (int x = 0; x < rows; x++) {
            for (int z = 0; z < columns; z++) {
                int y = columns * x;
                try {
                    newArrayContent[x][z] = list.get(y + z);
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    newArrayContent[x][z] = null;
                }

            }
        }
        return newArrayContent;
    }

    private static List<Partner> sanitizeList(List<Partner> list) {
        List<Partner> otherList = new ArrayList<>();
        for (Partner p : list) {
            if (p != null) {
                otherList.add(p);
            }
        }
        return otherList;
    }
}
