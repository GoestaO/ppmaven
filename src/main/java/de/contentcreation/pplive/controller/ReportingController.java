/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.LineChartModel;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.reporting.ExcelGenerator;
import de.contentcreation.pplive.reportingClasses.LeistungsreportNoUser;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung1;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung2;
import de.contentcreation.pplive.reportingClasses.RejectReportOverview;
import de.contentcreation.pplive.reportingClasses.UserReport;
import de.contentcreation.pplive.services.DatabaseHandler;
import de.contentcreation.pplive.services.ReportingHandler;
import de.contentcreation.pplive.util.DateHelper;
import de.contentcreation.pplive.util.QueryHelper;

/**
 * Dies ist der Controller für die Reporting-Seite.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@SessionScoped
public class ReportingController implements Serializable {

    @EJB
    private ReportingHandler rh;

    @EJB
    private DatabaseHandler dh;

    @Inject
    private ExcelGenerator ex;

    @Inject
    private ChartController chartController;
    
    @Inject
    private OverviewController overviewController;

    private Date leistungDate1;

    private Date leistungDate2;

    private String leistungsReportSelectedStatus;

    private Date uploadDate1;

    private Date uploadDate2;

    private List<Integer> selectedPartner;

    private List<Integer> partnerList;

    private int offen;

    private Date rejectedDate1;

    private Date rejectedDate2;

    private List<User> userList;

    private List<User> selectedUsers;

    private List<Object[]> weeklyUserStatistic;

    private List<Object[]> dailyUserStatistic;

    private List<LineChartModel> cwUserChartList;

    private List<LineChartModel> dailyUserChartList;

    private LineChartModel cwOverviewChart;

    private LineChartModel cwUserChart;

    private LineChartModel dailyOverviewChart;

    // Getter + Setter
    public Date getLeistungDate1() {
        return leistungDate1;
    }

    public void setLeistungDate1(Date leistungDate1) {
        this.leistungDate1 = leistungDate1;
    }

    public Date getLeistungDate2() {
        return leistungDate2;
    }

    public void setLeistungDate2(Date leistungDate2) {
        this.leistungDate2 = leistungDate2;
    }

    public String getLeistungsReportSelectedStatus() {
        return leistungsReportSelectedStatus;
    }

    public void setLeistungsReportSelectedStatus(String leistungsReportSelectedStatus) {
        this.leistungsReportSelectedStatus = leistungsReportSelectedStatus;
    }

    public Date getUploadDate1() {
        return uploadDate1;
    }

    public void setUploadDate1(Date uploadDate1) {
        this.uploadDate1 = uploadDate1;
    }

    public Date getUploadDate2() {
        return uploadDate2;
    }

    public void setUploadDate2(Date uploadDate2) {
        this.uploadDate2 = uploadDate2;
    }

    public List<Integer> getSelectedPartner() {
        return selectedPartner;
    }

    public void setSelectedPartner(List<Integer> selectedPartner) {
        this.selectedPartner = selectedPartner;
    }

    public List<Integer> getPartnerList() {
        return dh.getPartner2();
    }

    public int getOffen() {
        return offen;
    }

    public void setOffen(int offen) {
        this.offen = offen;
    }

    public void setPartnerList(List<Integer> partnerList) {
        this.partnerList = partnerList;
    }

    public Date getRejectedDate1() {
        return rejectedDate1;
    }

    public void setRejectedDate1(Date rejectedDate1) {
        this.rejectedDate1 = rejectedDate1;
    }

    public Date getRejectedDate2() {
        return rejectedDate2;
    }

    public void setRejectedDate2(Date rejectedDate2) {
        this.rejectedDate2 = rejectedDate2;
    }

    public List<User> getUserList() {
        if (userList == null) {
            userList = this.loadUsersByDate();
        }
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<Object[]> getWeeklyUserStatistic() {
        return weeklyUserStatistic;
    }

    public void setWeeklyUserStatistic(List<Object[]> weeklyUserStatistic) {
        this.weeklyUserStatistic = weeklyUserStatistic;
    }

    public List<Object[]> getDailyUserStatistic() {
        return dailyUserStatistic;
    }

    public void setDailyUserStatistic(List<Object[]> dailyUserStatistic) {
        this.dailyUserStatistic = dailyUserStatistic;
    }

    public List<LineChartModel> getDailyUserChartList() {
        return dailyUserChartList;
    }

    public void setDailyUserChartList(List<LineChartModel> dailyUserChartList) {
        this.dailyUserChartList = dailyUserChartList;
    }

    public LineChartModel getCwOverviewChart() {
        return cwOverviewChart;
    }

    public void setCwOverviewChart(LineChartModel cwOverviewChart) {
        this.cwOverviewChart = cwOverviewChart;
    }

    public List<LineChartModel> getCwUserChartList() {
        return cwUserChartList;
    }

    public void setCwUserChartList(List<LineChartModel> cwUserChartList) {
        this.cwUserChartList = cwUserChartList;
    }

    public LineChartModel getDailyOverviewChart() {
        return dailyOverviewChart;
    }

    public void setDailyOverviewChart(LineChartModel dailyOverviewChart) {
        this.dailyOverviewChart = dailyOverviewChart;
    }

    public LineChartModel getCwUserChart() {
        return cwUserChart;
    }

    public void setCwUserChart(LineChartModel cwUserChart) {
        this.cwUserChart = cwUserChart;
    }

//    Methoden
    private List<User> loadUserList() {
        return rh.getUsers();
    }

    private List<User> loadUsersByDate() {        
        if (this.leistungDate1 != null && this.leistungDate2 != null) {
            return rh.getUsersByDate(this.leistungDate1, shiftDate(this.leistungDate2));
        } else if (this.leistungDate1 != null) {
            return rh.getUsersByDate(this.leistungDate1);
        }
        return null;
    }

    public void onSelectedDate(SelectEvent event) {
        userList = loadUsersByDate();
    }

    /**
     * Diese Methode gibt für einen gewählten Zeitraum alle Buchungen aus.
     *
     * @param datum1 Das erste Datum = Start-Datum
     * @param datum2 Das zweite Datum = End-Datum
     */
    public void getLeistungsReport(Date datum1, Date datum2) {
        if (datum2 == null) {
            datum2 = new Date();
        }

        if (datum2.before(datum1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
        } else {
            datum2 = this.shiftDate(datum2);
            List<UserReport> userReport = rh.getUserReport(datum1, datum2);

            String fileName = "UserReport.xlsx";
            File reportFile = new File(fileName);
            ex.createUserReport(reportFile, userReport);
            try {
                download(reportFile);
            } catch (IOException ex) {
                Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

     /**
     * Diese Methode gibt für einen gewählten Zeitraum alle Buchungen aus.
     *
     * @param datum1 Das erste Datum = Start-Datum
     * @param datum2 Das zweite Datum = End-Datum
     */
    public void getLeistungsReportNoUsers(Date datum1, Date datum2) {
        if (datum2 == null) {
            datum2 = new Date();
        }

        if (datum2.before(datum1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
        } else {
            datum2 = this.shiftDate(datum2);
            List<LeistungsreportNoUser> Leistungsreport = rh.getLeistungsreportNoUser(datum1, datum2);

            String fileName = "Leistungsreport.xlsx";
            File reportFile = new File(fileName);
            ex.createLeistungsreportNoUser(reportFile, Leistungsreport);
            try {
                download(reportFile);
            } catch (IOException ex) {
                Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Diese Methode gibt für einen gewählten Zeitraum alle Buchungen aus.
     *
     * @param datum1 Das erste Datum = Start-Datum
     * @param datum2 Das zweite Datum = End-Datum
     */
    public void getRejectReport(Date datum1, Date datum2) {
        if (datum2 == null) {
            datum2 = new Date();
        }
        String date1String = getDateString(datum1);
        String date2String = getDateString(datum2);
        String nativeDate1 = DateHelper.getFirstDate(datum1);
        String nativeDate2 = DateHelper.getSecondDate(datum2);

        if (datum2.before(datum1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
        } else {
            datum2 = this.shiftDate(datum2);

            List<RejectReportOverview> overviewList = rh.getRejectReportOverview(datum1, datum2);
            List<RejectReportBemerkung1> bemerkung1List = rh.getRejectReportBemerkung1(nativeDate1, nativeDate2);
            List<RejectReportBemerkung2> bemerkung2List = rh.getRejectReportBemerkung2(nativeDate1, nativeDate2);

            String fileName = "RejectReport_" + date1String + "_" + date2String + ".xlsx";
            File reportFile = new File(fileName);
            ex.createRejectReport(reportFile, overviewList, bemerkung1List, bemerkung2List);
            try {
                download(reportFile);
            } catch (IOException ex) {
                Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Diese Methode gibt alle offenen Backlog-Artikel aus
     */
    public void getPartnerReportList() {

        List<BacklogArticle> partnerReportList = rh.getOpenBacklogArticles();
        String fileName = "Offene_Artikel.xlsx";
        File reportFile = new File(fileName);
        ex.createPartnerReport(reportFile, partnerReportList);
        try {
            download(reportFile);

        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Diese Methode gibt den KAM-Report aus, gefiltert, ob die Artikel offen
     * oder fertig sind.
     *
     * @param offen 1 = Artikel offen, 0 = Artikel fertig
     */
    public void getKAMReportList(int offen) {
        List<Object[]> partnerReport = rh.getKAMReportArticles(offen);
        String fileName = "KeyAccountReport.xlsx";
        File reportFile = new File(fileName);
        ex.createKeyAccountReport(reportFile, partnerReport);
        try {
            download(reportFile);

        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Diese Methode gibt alle bereits bearbeiteten Artikel für eine Auswahl von
     * Partnern aus.
     *
     * @param selectedPartner Die gewählten Partner.
     */
    public void getEditArticlesReport(List<Integer> selectedPartner) {
        List<Object[]> editedArticlesList = rh.getEditedDataByPartner(selectedPartner);
        String fileName = "GepflegteArtikel_Partner" + selectedPartner + ".xlsx";
        File reportFile = new File(fileName);
        ex.createEditedArticlesReport(reportFile, editedArticlesList);
        try {
            download(reportFile);

        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Diese Methode gibt alle neu hochgeladenen (neu in der Backlog
     * erschienenen) Artikel für einen gewählten Zeitraum aus.
     *
     * @param datum1 Das erste Datum = Start-Datum
     * @param datum2 Das zweite Datum = End-Datum
     */
    public void getNewArticlesReport(Date datum1, Date datum2) {
        if (datum2.before(datum1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
        } else {
            datum2 = this.shiftDate(datum2);
            String fileName = "neueArtikel.xlsx";
            File reportFile = new File(fileName);
            ex.createNewArticlesReport(reportFile, datum1, datum2);
            try {
                download(reportFile);

            } catch (IOException ex) {
                Logger.getLogger(ReportingController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void exportCurrentSelection(List<BacklogArticle> backlogList){
    	String fileName="currentSelection.xlsx";
    	File reportFile = new File(fileName);
    	ex.exportCurrentSelection(reportFile, backlogList);
    	 try {
             download(reportFile);

         } catch (IOException ex) {
             Logger.getLogger(ReportingController.class
                     .getName()).log(Level.SEVERE, null, ex);
         }
    }
  

    /**
     * Diese Methode verschiebt ein Datum um einen Tag nach hinten, ist wichtig,
     * um bei einer Zeitraumsabfrage das End-Datum auch noch in der Abfrage zu
     * haben.
     *
     * @param date
     * @return Das Datumsobjekt.
     */
    public Date shiftDate(Date date) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.add(5, 1);
        Date secondDate = cal2.getTime();
        return secondDate;
    }

    /**
     * Diese Methode ist dafür zuständig, eine Datei von der Seite
     * herunterzuladen.
     *
     * @param file Die Datei.
     * @throws IOException
     */
    public void download(File file) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
//        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.

        ec.setResponseContentLength((int) file.length()); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[10000];
        int bytesRead = 0;
        while ((bytesRead = fis.read(buffer)) > 0) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        fis.close();
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

//    public void downloadFile(File file) {
//
//        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//
//        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName() + ".xlsx");
//        response.setContentLength((int) file.length());
//        ServletOutputStream out = null;
//        try {
//            FileInputStream input = new FileInputStream(file);
//            byte[] buffer = new byte[1024];
//            out = response.getOutputStream();
//            int i = 0;
//            while ((i = input.read(buffer)) != -1) {
//                out.write(buffer);
//                out.flush();
//            }
//            FacesContext.getCurrentInstance().getResponseComplete();
//        } catch (IOException err) {
//            err.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException err) {
//                err.printStackTrace();
//            }
//        }
//    }
    private String getDateString(Date date) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }

    public void click(String page) {

        RequestContext requestContext = RequestContext.getCurrentInstance();

        // Weiterleitung
        requestContext.execute("window.open('" + page + "', '_blank');");
    }

    public void generateLeistungsReport2() {
        if (this.leistungDate2 == null) {
            this.leistungDate2 = new Date();
        }
        if (this.leistungDate1 == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlendes Datum", "Bitte ein Startdatum wählen"));
        }

        if (this.selectedUsers == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kein Nutzer ausgewählt", "Bitte mindestens einen Nutzer zur Analyse auswählen."));
        }
        if (this.leistungsReportSelectedStatus == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlender Status", "Bitte einen Status wählen."));
        }

        if (this.selectedUsers != null && this.leistungDate1 != null && this.leistungsReportSelectedStatus != null) {
            if (this.leistungDate2.before(this.leistungDate1)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
            }
            leistungDate2 = new Date();
            weeklyUserStatistic = this.loadWeeklyOverview(leistungDate1, leistungDate2, selectedUsers);
            dailyUserStatistic = this.loadDailyOverview(leistungDate1, leistungDate2, selectedUsers);
            cwOverviewChart = loadWeeklyOverviewChart(leistungDate1, leistungDate2, selectedUsers, leistungsReportSelectedStatus);
            cwUserChartList = loadWeeklyUserChartList(leistungDate1, leistungDate2, selectedUsers);
            dailyOverviewChart = loadDailyOverviewChart(leistungDate1, leistungDate2, selectedUsers, leistungsReportSelectedStatus);
            dailyUserChartList = loadDailyUserChartList(leistungDate1, leistungDate2, selectedUsers);
            click("leistungsreport.jsf");
        }
    }

    public List<Object[]> loadWeeklyOverview(Date datum1, Date datum2, List<User> userList) {
        String datum1String = DateHelper.getFirstDate(datum1);
        String datum2String = DateHelper.getSecondDate(datum2);
        String userListString = QueryHelper.getInClauseUserList(userList);
        return rh.getWeeklyUserOverview(datum1String, datum2String, userListString);
    }

    public List<Object[]> loadDailyOverview(Date datum1, Date datum2, List<User> userList) {
        String datum1String = DateHelper.getFirstDate(datum1);
        String datum2String = DateHelper.getSecondDate(datum2);
        String userListString = QueryHelper.getInClauseUserList(userList);
        return rh.getDailyUserOverview(datum1String, datum2String, userListString);
    }

    public LineChartModel loadWeeklyOverviewChart(Date datum1, Date datum2, List<User> userList, String status) {
        int week = DateHelper.getCalenderWeek(datum1);
        String datum1String = DateHelper.getFirstDate(datum1);
        String datum2String = DateHelper.getSecondDate(datum2);
        String userListString = QueryHelper.getInClauseUserList(userList);
        List<Object[]> data = rh.getWeeklyOverviewChart(datum1String, datum2String, userListString, status);
        LineChartModel chart = chartController.createLinechartModel(data, false, week, "Übersicht für ausgewählte Nutzer - Buchungen '" + status + "'", "Kalenderwoche", "Anzahl");
        return chart;
    }

//    private LineChartModel loadUserChart(Date datum1, Date datum2, User user) {
//        String datum1String = DateHelper.getFirstDate(datum1);
//        String datum2String = DateHelper.getSecondDate(datum2);
//        String userListString = "(" + String.valueOf(user.getId()) + ")";
//        List<Object[]> data = rh.getKWNutzerChart(datum1String, datum2String, userListString);
//        String nameLabel = user.getVorname().toUpperCase().concat(" ").concat(user.getNachname().toUpperCase());
//        LineChartModel chart = chartController.createLinechartModel(data, true, "Statistik für " + nameLabel, "Kalenderwoche", "Anzahl");
//        return chart;
//    }
    private List<LineChartModel> loadWeeklyUserChartList(Date datum1, Date datum2, List<User> userList) {
        int week = DateHelper.getCalenderWeek(datum1);
        List<LineChartModel> chartList = new ArrayList<>();
        for (User user : userList) {
            String datum1String = DateHelper.getFirstDate(datum1);
            String datum2String = DateHelper.getSecondDate(datum2);
            String userListString = "(" + String.valueOf(user.getId()) + ")";
            List<Object[]> data = rh.getWeeklyUserChart(datum1String, datum2String, userListString);
            LineChartModel chart = chartController.createLinechartModel(data, true, week, "Statistik für " + user, "Kalenderwoche", "Anzahl");
            if (chart != null) {
                chartList.add(chart);
            }
        }
        return chartList;
    }

    private LineChartModel loadDailyOverviewChart(Date datum1, Date datum2, List<User> userList, String status) {
        int week = DateHelper.getCalenderWeek(datum1);
        List<LineChartModel> chartList = new ArrayList<>();
        String datum1String = DateHelper.getFirstDate(datum1);
        String datum2String = DateHelper.getSecondDate(datum2);
        String userListString = QueryHelper.getInClauseUserList(userList);
        List<Object[]> data = rh.getDailyOverviewChart(datum1String, datum2String, userListString, status);
        LineChartModel chart = chartController.createDateLinechartModel(data, false, 0, "Übersicht für ausgewählte Nutzer - Buchungen '" + status + "'", "Datum", "Anzahl");
        return chart;
    }

    private List<LineChartModel> loadDailyUserChartList(Date datum1, Date datum2, List<User> userList) {
        int week = DateHelper.getCalenderWeek(datum1);
        List<LineChartModel> chartList = new ArrayList<>();
        for (User user : userList) {
            String datum1String = DateHelper.getFirstDate(datum1);
            String datum2String = DateHelper.getSecondDate(datum2);
            String userListString = "(" + String.valueOf(user.getId()) + ")";
            List<Object[]> data = rh.getDailyUserChart(datum1String, datum2String, userListString);
            LineChartModel chart = chartController.createDateLinechartModel(data, true, 0, "Statistik für " + user, "Datum", "Anzahl");
            if (chart != null) {
                chartList.add(chart);
            }
        }
        return chartList;
    }

    public void postProcessXLSX(Object document) {
        XSSFWorkbook wb = (XSSFWorkbook) document;
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow header = sheet.getRow(0);

        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (int i = 0; i < 11; i++) {
//            XSSFCell cell = header.getCell(i);
            sheet.autoSizeColumn(i);
//            cell.setCellStyle(cellStyle);
        }
    }

}
