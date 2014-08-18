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

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
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

//    @PostConstruct
//    public void init() {
//        partnerList = 
//    }
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

    public String login(String username, String password, List<Integer> partnerList) {
        String direction = "";
        User user = service.login(username, password);
        if (user != null) {
//            welcome();
            bean.setUser(user);
            bean.setValid(true);
            bean.setVorname(username);
            bean.setNachname(username);
            bean.setPartnerList(partnerList);
//            RequestContext requestContext = RequestContext.getCurrentInstance();
//            requestContext.execute("document.location.reload(true)");

            direction = "backlogOverview.jsf?faces-redirect=true";
        } else if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login nicht erfolgreich", "Benutzername '" + username + "' oder Passwort ist nicht korrekt!"));
            direction = null;
        }
        return direction;
    }
    
}
