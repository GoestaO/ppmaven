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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
//import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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

    private String offen;

    @EJB
    private DatabaseHandler dh;

    private List<BacklogArticle> backlogList;

    @Inject
    private UserBean userBean;

    @PostConstruct
    public void init() {
        backlogList = dh.getBacklog();
    }

    public List<BacklogArticle> getBacklogList() {
        return backlogList;
    }

    public String getOffen() {
        return offen;
    }

    public void setOffen(String offen) {
        this.offen = offen;
    }

    public void onRowEdit(RowEditEvent event) {
        BacklogArticle editedArticle = (BacklogArticle) event.getObject();

        String identifier = editedArticle.getIdentifier();
        String bemerkung1 = editedArticle.getBemerkung1();
        String bemerkung2 = editedArticle.getBemerkung2();
        String bemerkung3 = editedArticle.getBemerkung3();
        String bemerkungKAM = editedArticle.getBemerkungKAM();
//        boolean neuerStatus = editedArticle.isOffen();
        User currentUser = userBean.getUser();
        String season = editedArticle.getSaison();
        dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3, bemerkungKAM, bemerkung3, currentUser, season);
        FacesMessage msg = new FacesMessage("Artikel bearbeitet", ((BacklogArticle) event.getObject()).getIdentifier());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Bearbeitung abgebrochen", "Bearbeitung abgebrochen.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
