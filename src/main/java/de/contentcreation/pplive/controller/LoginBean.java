/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.services.UserService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import de.contentcreation.pplive.model.UserBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */
@Named
@RequestScoped
public class LoginBean {

//    @PersistenceContext
//    private EntityManager em;
    @Inject
    private UserBean bean;

    @EJB
    private UserService service;

    private String username;

    private String password;

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

    public void login(String username, String password) {
        if (service.login(username, password) != null) {
            bean.setValid(true);
            bean.setVorname(username);
            bean.setNachname(username);
            FacesMessage message = new FacesMessage("Login erfolgreich", "Willkommen " + username + ".");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

}
