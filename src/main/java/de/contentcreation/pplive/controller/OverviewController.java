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
import de.contentcreation.pplive.websockets.BacklogClientEndpoint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.LineChartModel;

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

    DataTable dataTable;

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

    Map<String, Object> filterValues;

    Map<String, Object> storedFilterValues;

    @Inject
    private UserBean userBean;

    private List<LineChartModel> userChartList;

    private LineChartModel allUsersChart;

    @PostConstruct
    public void init() {
        BacklogClientEndpoint.BacklogArticleMessageHandler messageHandler = new BacklogClientEndpoint.BacklogArticleMessageHandler() {
            @Override
            public void handleMessage(BacklogArticle article) {
                System.out.println("Handling message: " + article.getBemerkung1());
                for (BacklogArticle a : backlogList) {
                    if (a.identifierEquals(article)) {
                        a = article;
                    }
                }               
            }

        };
        userBean.getClient().addBacklogArticleMessageHandler(messageHandler);
    }

    // Getter und Setter
    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

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

    public Map<String, Object> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Map<String, Object> filterValues) {
        this.filterValues = filterValues;
    }

    public List<LineChartModel> getUserChartList() {
        return userChartList;
    }

    public void setUserChartList(List<LineChartModel> userChartList) {
        this.userChartList = userChartList;
    }

    public LineChartModel getAllUsersChart() {
        return allUsersChart;
    }

    public void setAllUsersChart(LineChartModel allUsersChart) {
        this.allUsersChart = allUsersChart;
    }

    /**
     * Diese Methode lädt die Daten, die in der Tabelle dargestellt werden
     * sollen.
     *
     * @return Tabellendaten
     */
    private List<BacklogArticle> loadData() {
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
        try {
            if (this.selectedArticle != null) {
                this.selectedArticle.setBemerkung3(bemerkung);
            }

        } catch (NullPointerException ex) {

        }

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

//    public void finishAll(){
//        for (BacklogArticle b : selectedArticles){
//            b.setOffen(false);
//        }        
//    }
    /**
     * Diese Methode ist für die Aktualisierung der im Bearbeitungsdialog
     * bearbeiteten Artikel zuständig. Dazu werden alle Nutzereingaben bezogen
     * und an den Datenbank-Handler weitergereicht, der sie dann abspeichert.
     *
     * @param event
     */
    public void updateHandler(ActionEvent event) {

        List<BacklogArticle> refusedList = new ArrayList<>();
        List<BacklogArticle> updatedList = new ArrayList<>();
        List<BacklogArticle> finishedList = new ArrayList<>();
        for (BacklogArticle editedArticle : selectedArticles) {
            String identifier = editedArticle.getIdentifier();
            String bemerkung1 = editedArticle.getBemerkung1();
            bemerkung1 = (bemerkung1 == null) ? null : bemerkung1.trim();

            String bemerkung2 = editedArticle.getBemerkung2();
            bemerkung2 = (bemerkung2 == null) ? null : bemerkung2.trim();

            String bemerkung3 = editedArticle.getBemerkung3();
            bemerkung3 = (bemerkung3 == null) ? null : bemerkung3.trim();

            String bemerkungKAM = editedArticle.getBemerkungKAM();
            bemerkungKAM = (bemerkungKAM == null) ? null : bemerkungKAM.trim();

            boolean neuerStatus = editedArticle.isOffen();
            User currentUser = userBean.getUser();
            String season = editedArticle.getSaison();

            BacklogArticle usprungsArtikel = dh.getBacklogArticle(identifier);

            // Wenn Status auf "fertig" gesetzt, dann auf finishedList setzen und Updatebuchung durchführen
            if (neuerStatus == false) {
                finishedList.add(editedArticle);
                userBean.getClient().sendMessage(editedArticle);
                dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3, bemerkungKAM, neuerStatus, currentUser, season);

                // Schauen, ob es bei dem Artikel Veränderungen gab, wenn es keine gab, keine Buchung durchführen
            } else if (neuerStatus && usprungsArtikel.equals(editedArticle) || usprungsArtikel.equalsWithNulls(editedArticle)) {
                refusedList.add(editedArticle);
            } else {
                updatedList.add(editedArticle);
                userBean.getClient().sendMessage(editedArticle);
                dh.updateArticleStatus(identifier, bemerkung1, bemerkung2, bemerkung3, bemerkungKAM, neuerStatus, currentUser, season);
            }

            // Die Filterwerte aus der Tabelle ziehen und in der Session speichern
            filterValues = dataTable.getFilters();

            // Die aktuellen Filterwerte sichern
            storedFilterValues = filterValues;

            // Den aktuellen Filter null setzen
            filterValues = null;

            // Aktualisierung der Tabellen-Daten
            backlogList = this.loadData();

            // Filter wieder reinsetzen und Tabelle filtern.
            filterValues = storedFilterValues;
            RequestContext.getCurrentInstance().execute("PF('dataTable').filter();");

        }

        // Bestätigungsnachricht generieren und ausgeben
        String message = generateMessage(finishedList, updatedList, refusedList);

        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Zusammenfassung", message));
    }

    // Wenn Nutzersession abgelaufen ist, wird diese Nachricht angezeigt.
    public void fatal() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nicht eingeloggt!", "Du musst dich erst einloggen, bevor du loslegen kannst."));
    }

    /**
     * Generiert eine Feedbacknachricht für den Nutzer, welche Artikel
     * geupdated, welche ignoriert und welche gefinished wurden.
     *
     * @param finishedList
     * @param updatedList
     * @param refusedList
     * @return
     */
    private String generateMessage(List<BacklogArticle> finishedList, List<BacklogArticle> updatedList, List<BacklogArticle> refusedList) {
        StringBuilder message = new StringBuilder("<html><body>");

        if (finishedList.size() > 0) {
            message.append(
                    "<p>").append(finishedList).append(" abgeschlossen </p>");
        }

        if (updatedList.size() > 0) {
            message.append(
                    "<p>").append(updatedList).append(" aktualisiert </p>");
        }

        if (refusedList.size() > 0) {
            message.append(
                    "<p>").append(refusedList).append(" nicht aktualisiert, da keine Veränderungen vorgenommen wurden. </p>");
        }

        message.append(
                "</body></html>");

        return message.toString();
    }

    /**
     * Diese Methode wandelt ein einzelnes BacklogArticle-Objekt in eine Liste
     * mit dem Objekt um
     *
     * @param article Das BacklogArticle-Objekt
     * @return Die Liste *
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
            if (bemerkung.toLowerCase().contains(query.toLowerCase())) {
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
            if (bemerkung.toLowerCase().contains(query.toLowerCase())) {
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

    public void clearFilters() {
        RequestContext.getCurrentInstance().execute("PF('dataTable').clearFilters();");
        filterValues = null;
    }

}
