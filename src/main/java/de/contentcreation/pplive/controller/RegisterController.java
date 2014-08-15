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
 *
 * @author goesta
 */
@Named
@RequestScoped
public class RegisterController implements Serializable{

    @EJB
    private UserService service;

    private String username;

    private String password;

    private String vorname;

    private String nachname;

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
