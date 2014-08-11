package de.contentcreation.pplive.model;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private int id;
    private String nick;
    private String vorname;
    private String nachname;
    private String passwort;
    private boolean isValid;
    private User user;
    private List<Integer> partnerList;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean newValid) {
        this.isValid = newValid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Integer> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<Integer> partnerList) {
        this.partnerList = partnerList;
    }
    
    public String resetUser() {
        this.setNick(null);
        this.setPasswort(null);
        this.setValid(false);
        this.setPartnerList(null);
        FacesMessage message = new FacesMessage("Logout", "Logout war erfolgreich.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "login.jsf?faces-redirect = true";
    }
}
