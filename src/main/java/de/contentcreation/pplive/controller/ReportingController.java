/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.reporting.ExcelGenerator;
import de.contentcreation.pplive.reportingClasses.UserReport;
import de.contentcreation.pplive.services.DatabaseHandler;
import de.contentcreation.pplive.services.ReportingHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Dies ist der Controller für die Reporting-Seite.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@RequestScoped
public class ReportingController implements Serializable {

    @EJB
    private ReportingHandler rh;

    @EJB
    private DatabaseHandler dh;

    @Inject
    private ExcelGenerator ex;

    private Date leistungDate1;

    private Date leistungDate2;

    private Date uploadDate1;

    private Date uploadDate2;

    private List<Integer> selectedPartner;

    private List<Integer> partnerList;

    private int offen;

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

    /**
     * Diese Methode gibt für einen gewählten Zeitraum alle Buchungen aus.
     *
     * @param datum1 Das erste Datum = Start-Datum
     * @param datum2 Das zweite Datum = End-Datum
     */
    public void getLeistungsReport(Date datum1, Date datum2) {
        if (datum2.before(datum1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehlerhafte Eingabe", "Das erste Datum muss kleiner als das zweite Datum sein. Bitte versuche es noch einmal."));
        } else {
            datum2 = this.shiftDate(datum2);
            List<UserReport> userReport = rh.getUserReport(datum1, datum2);

            String fileName = "UserReport";
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
     * Diese Methode gibt alle offenen Backlog-Artikel aus
     */
    public void getPartnerReportList() {

        List<BacklogArticle> partnerReportList = rh.getOpenBacklogArticles();
        String fileName = "Offene_Artikel";
        File reportFile = new File(fileName);
        ex.createPartnerReport(reportFile, partnerReportList);
        try {
            download(reportFile);
        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Diese Methode gibt den KAM-Report aus, gefiltert, ob die Artikel offen
     * oder fertig sind.
     *
     * @param offen 1 = Artikel offen, 0 = Artikel fertig
     */
    public void getKAMReportList(int offen) {
        List<Object[]> partnerReport = rh.getProblemArticles(offen);
        String fileName = "KeyAccountReport";
        File reportFile = new File(fileName);
        ex.createKeyAccountReport(reportFile, partnerReport);
        try {
            download(reportFile);
        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
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

        String fileName = "GepflegteArtikel_Partner" + selectedPartner;
        File reportFile = new File(fileName);
        ex.createEditedArticlesReport(reportFile, editedArticlesList);
        try {
            download(reportFile);
        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
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
            String fileName = "neueArtikel";
            File reportFile = new File(fileName);
            ex.createNewArticlesReport(reportFile, datum1, datum2);
            try {
                download(reportFile);
            } catch (IOException ex) {
                Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * Diese Methode ist dafür zuständig, eine Datei von der Seite herunterzuladen.
     * @param file Die Datei.
     * @throws IOException 
     */
    public void download(File file) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType("application/vnd.ms-excel"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.

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

}
