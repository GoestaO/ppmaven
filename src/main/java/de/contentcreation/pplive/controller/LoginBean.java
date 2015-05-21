/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

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
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.CacheStoreMode;
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

    public List<Integer> getSelectedPartners() {
        return selectedPartners;
    }

    public void setSelectedPartners(List<Integer> selectedPartners) {
        this.selectedPartners = selectedPartners;
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

    /**
     * Diese Methode führt den Abgleich der eingegebenen Daten auf der
     * Login-Seite mit in der DB hinterlegen Daten durch, indem auf den
     * UserService zugegriffen wird und geschaut wird, ob ein Eintrag mit der
     * Kombination aus Nutzername + Passwort existiert.
     *
     * @param username Der eingegebene Nutzername.
     * @param password Das eingegebene Passwort.
     * @param partnerList Die gewählten Partner, die für eine Sitzung bearbeitet
     * werden sollen.
     * @return Die Seite, zu der am Ende der Methode navigiert werden soll,
     * entweder die Backlogübersicht, oder es gibt eine Fehlermeldung und keine
     * Navigation.
     */
    public String login(String username, String password, List<Integer> partnerList) {
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

    public String resetUser() {
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
        return "login.jsf?faces-redirect = true";
    }

}
