/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.services.UserService;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Dies ist der Controller für die Registrierungs-Seite. Er schaut nach, ob
 * schon ein Nutzer mit diesem Nutzernamen registriert ist, ansonsten wird der
 * Nutzer aufgefordert, einen anderen Nutzernamen zu wählen.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@RequestScoped
public class RegisterController implements Serializable {

    @EJB
    private UserService service;

    private String username;

    private String password;

    private String vorname;

    private String nachname;

    // Getter + Setter
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
        this.password = password;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Mit Hilfe der vom Nutzer eingegebenen Daten wird eine Abfrage vom
     * UserService durchgeführt, ob zunächst der Nutzername verfügbar ist. Ist
     * dies der Fall, werden die Daten gespeichert, ansonsten wird eine
     * Fehlermeldung ausgegeben und der Nutzer muss es erneut versuchen.
     *
     * @param username Der gewählte Nutzername
     * @param vorname Der genannte Vorname
     * @param nachname Der genannte Nachname
     * @param password Das gewählte Passwort
     * @return Die Navigation nach Ausführung der Methode - entweder zum Login
     * (wenn Registrierung erfolgreich), oder nochmals zur Registrierung, wenn
     * Nutzername bereits vorhanden ist.
     */
    public String registerUser(String username, String vorname, String nachname,
            String password) {
        String navigation = "";
        boolean registered = service.registerUser(username, vorname, nachname, password);

        if (!registered) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nutzername bekannt", "Dieser Nutzername ist bereits vergeben, bitte versuche einen anderen!");
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
            navigation = "register.jsf";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrierung erfolgreich", "Du wurdest erfolgreich registriert.");
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
            navigation = "login.jsf";

        }
        return navigation;

    }

    /**
     * Diese Method ist für die Verschlüsselung des eingegebenen Passwortes in
     * einen MD5-Hash zuständig
     *
     * @param input Das eingegebene Passwort
     * @return Der verschlüsselte MD5-Hash
     */
    public String md5(String input) {
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

}
