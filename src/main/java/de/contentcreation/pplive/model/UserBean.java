package de.contentcreation.pplive.model;

import java.io.Serializable;
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

    public String resetUser() {
        this.setValid(false);
        FacesMessage message = new FacesMessage("Logout", "Logout war erfolgreich.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "login.jsf?faces-redirect = true";
    }
}
