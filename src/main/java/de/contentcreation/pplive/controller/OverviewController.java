/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.model.UserBean;
import de.contentcreation.pplive.services.DatabaseHandler;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */
@Named
@ViewScoped
public class OverviewController implements Serializable {

    @EJB
    private DatabaseHandler dh;

    private List<BacklogArticle> backlogList;

    private List<BacklogArticle> selectedArticles;

    private List<BacklogArticle> filteredArticles;

    private HtmlDataTable myDataTable;

    private BacklogArticle selectedArticle;

    @Inject
    private UserBean userBean;

    public List<BacklogArticle> getBacklogList() {
        List<Integer> partnerList = userBean.getPartnerList();
        return dh.getBacklogByPartner2(partnerList);
    }

    public List<BacklogArticle> getSelectedArticles() {
        return selectedArticles;
    }

    public void setSelectedArticles(List<BacklogArticle> selectedArticles) {
        this.selectedArticles = selectedArticles;
    }

    public BacklogArticle getSelectedArticle() {
        return selectedArticle;
    }

    public void setSelectedArticle(BacklogArticle selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public HtmlDataTable getMyDataTable() {
        return myDataTable;
    }

    public void setMyDataTable(HtmlDataTable myDataTable) {
        this.myDataTable = myDataTable;
    }

    public List<BacklogArticle> getFilteredArticles() {
        return filteredArticles;
    }

    public void setFilteredArticles(List<BacklogArticle> filteredArticles) {
        this.filteredArticles = filteredArticles;
    }

    public void onRowEdit(RowEditEvent event) {
        BacklogArticle editedArticle = (BacklogArticle) event.getObject();

        String identifier = editedArticle.getIdentifier();
        String bemerkung1 = editedArticle.getBemerkung1();
        String bemerkung2 = editedArticle.getBemerkung2();
        String bemerkung3 = editedArticle.getBemerkung3();
        String bemerkungKAM = editedArticle.getBemerkungKAM();
        boolean neuerStatus = editedArticle.isOffen();
        User currentUser = userBean.getUser();
        String season = editedArticle.getSaison();
        dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3, bemerkungKAM, neuerStatus, currentUser, season);

    }

    public void update(List<BacklogArticle> selectedArticles) {

        for (BacklogArticle editedArticle : selectedArticles) {
            String identifier = editedArticle.getIdentifier();
            String bemerkung1 = editedArticle.getBemerkung1();
            String bemerkung2 = editedArticle.getBemerkung2();
            String bemerkung3 = editedArticle.getBemerkung3();
            String bemerkungKAM = editedArticle.getBemerkungKAM();
            boolean neuerStatus = editedArticle.isOffen();
            User currentUser = userBean.getUser();
            String season = editedArticle.getSaison();
            dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3, bemerkungKAM, neuerStatus, currentUser, season);
        }

        FacesMessage msg = new FacesMessage("Artikel bearbeitet", "Artikel erfolgreich aktualisiert");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void fatal() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nicht eingeloggt!", "Du musst dich erst einloggen, bevor du loslegen kannst."));
    }

}