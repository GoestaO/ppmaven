/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.Rolle;
import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.services.DatabaseHandler;
import de.contentcreation.pplive.services.UserService;
import de.contentcreation.pplive.util.MD5Secure;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

/**
 *
 * @author gostendorf
 */
@Named
@ViewScoped
public class UsermanagementBean implements Serializable {

    @EJB
    private UserService userService;

    @EJB
    private DatabaseHandler dbHandler;

    private String userName, newPassword1, newPassword2;

    private List<User> searchResult;

    private User selectedUser;

    private Rolle role, newRole;

    private List<Rolle> availableRoles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<User> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<User> searchResult) {
        this.searchResult = searchResult;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Rolle getRole() {
        return role;
    }

    public void setRole(Rolle role) {
        this.role = role;
    }

    public Rolle getNewRole() {
        return newRole;
    }

    public void setNewRole(Rolle newRole) {
        this.newRole = newRole;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public List<Rolle> getAvailableRoles() {
        if (availableRoles == null) {
            loadAvailableRoles();
        }
        return availableRoles;
    }

    public void setAvailableRoles(List<Rolle> availableRoles) {
        this.availableRoles = availableRoles;
    }

    public void searchUser(String searchTerm) {
        searchResult = userService.findUsers(searchTerm);
        System.out.println("searchResult = " + searchResult.size());
    }

    /**
     * Verschlüsselt einen Input in einen MD5-Hash
     *
     * @param input Der Input
     * @return Der MD5-Hash
     */
    public String md5(String input) {
        MD5Secure md5 = new MD5Secure();
        String md5Password = md5.encode(input);
        return md5Password;
    }

    /**
     * Ändert das Passwort eines ausgewählten Nutzers und gibt Feedback an die
     * Seite zurück. Überprüft dabei, ob die beiden eingegebenen Passwörter
     * übereinstimmen.
     */
    public void changePassword() {
        if (selectedUser == null) {
            notifyUser(null, FacesMessage.SEVERITY_WARN, "Kein Nutzer ausgewählt", "Bitte einen Nutzer auswählen!");
            closeWidget("passwordChangeWidget");
        }
        if (!this.newPassword1.equals(this.newPassword2)) {
            notifyUser(null, FacesMessage.SEVERITY_FATAL, "Passwort wurde nicht geändert", "Die Passwörter stimmen nicht überein!");
        }
        if (this.newPassword1.equals(this.newPassword2) && selectedUser != null) {
            newPassword1 = md5(newPassword1);
            selectedUser.setPasswort(newPassword1);
            userService.update(selectedUser);

            notifyUser(null, FacesMessage.SEVERITY_INFO, "", "Das Passwort wurde abgeändert");
            closeWidget("passwordChangeWidget");
        }
    }

    public void informUser(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }

    private void notifyUser(String messageId, Severity severity, String header, String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(messageId, new FacesMessage(severity, header, message));
    }

    private void notifyUserDialog(Severity severity, String header, String message) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, header, message));
    }

    // Schließt den Dialog
    private void closeWidget(String widgetVar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + widgetVar + "').hide();");
    }

    private void loadAvailableRoles() {
        availableRoles = dbHandler.getAllRoles();
    }

    public void updateRole() {
        if (newRole.equals(selectedUser.getRolle())) {
            informUser("Neue Rolle stimmt mit der jetzigen überein. Es wird nichts verändert.");
        } else {
            selectedUser.setRolle(newRole);
            userService.update(selectedUser);
            notifyUser(null, FacesMessage.SEVERITY_INFO, "Update erfolgreich", "Die Rolle des Nutzers wurde angepasst.");
        }
    }
}
