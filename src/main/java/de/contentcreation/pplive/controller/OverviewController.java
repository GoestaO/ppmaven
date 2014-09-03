/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.Bemerkung;
import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.model.UserBean;
import de.contentcreation.pplive.services.DatabaseHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 * Diese Bean ist der Controller für die Backlog-Übersichtsseite. Sie ist für
 * die Darstellung des Backlogs sowie für die Entgegennahme von
 * Datenbearbeitungen, die der Nutzer tätigt, zuständig.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
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

    private List<Bemerkung> selectedBemerkungen;

    private Bemerkung selectedBemerkung;

    private List<String> seasons;

    @Inject
    private UserBean userBean;

//    public List<BacklogArticle> getBacklogList() {
//        List<Integer> partnerList = userBean.getPartnerList();
//        return dh.getBacklogByPartner2(partnerList);
//    }
    // Getter und Setter
    public List<BacklogArticle> getBacklogList() {
        if (backlogList == null) {
            backlogList = this.loadData();
        }
        return backlogList;
    }

    public void setBacklogList(List<BacklogArticle> backlogList) {
        this.backlogList = backlogList;
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

    public List<Bemerkung> getSelectedBemerkungen() {
        return selectedBemerkungen;
    }

    public void setSelectedBemerkungen(List<Bemerkung> selectedBemerkungen) {
        this.selectedBemerkungen = selectedBemerkungen;
    }

    public Bemerkung getSelectedBemerkung() {
        return selectedBemerkung;
    }

    public void setSelectedBemerkung(Bemerkung selectedBemerkung) {
        this.selectedBemerkung = selectedBemerkung;
    }

    public List<String> getSeasons() {
        return dh.getSeasons();
    }

    public void setSeasons(List<String> seasons) {
        this.seasons = seasons;
    }

    /**
     * Diese Methode lädt die Daten, die in der Tabelle dargestellt werden
     * sollen.
     *
     * @return Tabellendaten
     */
    public List<BacklogArticle> loadData() {
        List<Integer> partnerList = userBean.getPartnerList();
        return dh.getBacklogByPartner2(partnerList);
    }

    // Event-Handler
    /**
     * Diese Methode überwacht die Auswahl, die beim Autocomplete der Bemerkung
     * 1 gemacht wird.
     *
     * @param event Der Event zu der Auswahl
     */
    public void handleSelectedBemerkung1(SelectEvent event) {
        String bemerkung = (String) event.getObject();
        try {
            if (this.selectedArticle != null) {
                this.selectedArticle.setBemerkung1(bemerkung);
            }

        } catch (NullPointerException ex) {

        }
    }

    public void handleSelectedBemerkung2(SelectEvent event) {
        String bemerkung = (String) event.getObject();
        try {
            if (this.selectedArticle != null) {
                this.selectedArticle.setBemerkung2(bemerkung);
            }

        } catch (NullPointerException ex) {

        }
    }

    public void handleSelectedBemerkung3(SelectEvent event) {
        String bemerkung = (String) event.getObject();
        this.selectedArticle.setBemerkung3(bemerkung);
    }

    public void handleSelectedBemerkungKAM(SelectEvent event) {
        String bemerkung = (String) event.getObject();
        try {
            if (this.selectedArticle != null) {
                this.selectedArticle.setBemerkungKAM(bemerkung);
            }

        } catch (NullPointerException ex) {

        }
    }

    /**
     * Diese Methode ist für die Aktualisierung der im Bearbeitungsdialog
     * bearbeiteten Artikel zuständig. Dazu werden alle Nutzereingaben bezogen
     * und an den Datenbank-Handler weitergereicht, der sie dann abspeichert.
     *
     * @param selectedArticles Die ausgewählten Artikel, die im
     * Bearbeitungsdialog bearbeitet wurden.
     */
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

        // Bestätigungsnachricht, dass Bearbeitung erfolgreich war.
        FacesMessage msg = new FacesMessage("Artikel bearbeitet", "Artikel erfolgreich aktualisiert");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    // Wenn Nutzersession abgelaufen ist, wird diese Nachricht angezeigt.
    public void fatal() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nicht eingeloggt!", "Du musst dich erst einloggen, bevor du loslegen kannst."));
    }

    /**
     * Diese Methode wandelt ein einzelnes BacklogArticle-Objekt in eine Liste
     * mit dem Objekt um
     *
     * @param article Das BacklogArticle-Objekt
     * @return Die Liste
     */
    public List<BacklogArticle> toList(BacklogArticle article) {
        List<BacklogArticle> list = new ArrayList<>();
        list.add(article);
        return list;
    }

    /**
     * Diese Methode ist für das Autocomplete zuständig. Anhand der eingegebenen
     * Zeichenkette wird in der Datenbank ein Abgleich durchgeführt, welche
     * Einträge diese Zeichenkette enthalten. Dann wird diese Liste mit den
     * zutreffenden Einträgen zurückgegeben.
     *
     * @param query Die eingegebene Zeichenkette
     * @return Die Liste mit den zutreffenden Einträgen
     */
    public List<String> completeBemerkung1(String query) {
        List<String> allBemerkungen = dh.getBemerkungen1();
        List<String> filteredBemerkungen = new ArrayList<>();

        for (int i = 0; i < allBemerkungen.size(); i++) {
            String bemerkung = allBemerkungen.get(i);
            if (bemerkung.toLowerCase().contains(query)) {
                filteredBemerkungen.add(bemerkung);
            }
        }
        return filteredBemerkungen;
    }

    public List<String> completeBemerkung2(String query) {
        List<String> allBemerkungen = dh.getBemerkungen2();
        List<String> filteredBemerkungen = new ArrayList<>();

        for (int i = 0; i < allBemerkungen.size(); i++) {
            String bemerkung = allBemerkungen.get(i);
            if (bemerkung.toLowerCase().contains(query)) {
                filteredBemerkungen.add(bemerkung);
            }
        }
        return filteredBemerkungen;
    }

    public boolean filterByConfig(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        String name = value.toString().toUpperCase();
        filterText = filterText.toUpperCase();

        return name.contains(filterText);
    }

}
