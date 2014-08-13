/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.reporting.ExcelGenerator;
import de.contentcreation.pplive.reportingClasses.UserReport;
import de.contentcreation.pplive.services.ReportingHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */
@Named
@ViewScoped
public class ReportingController {

    @EJB
    private ReportingHandler rh;

    @Inject
    private ExcelGenerator ex;

    private Date date1;

    private Date date2;
    
    

    private List<BacklogArticle> leistungsReportList;

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    
    
    public void getLeistungsReport(Date date1, Date date2) {
        date2 = this.shiftDate(date2);
        System.out.println("date2 = " + date2);
        List<UserReport> userReport = rh.getUserReport(date1, date2);

        String fileName = "UserReport.xlsx";
        File reportFile = new File(fileName);
        ex.createUserReport(reportFile, userReport);
        try {
            download(reportFile);
        } catch (IOException ex) {
            Logger.getLogger(ReportingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLeistungsReportList(List<BacklogArticle> leistungsReportList) {
        this.leistungsReportList = leistungsReportList;
    }

    public Date shiftDate(Date date) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.add(5, 1);
        Date secondDate = cal2.getTime();       
        return secondDate;
    }

    public void download(File file) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        System.out.println("file = " + file.getName());
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

    public void downloadFile(File file) {

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName() + ".xlsx");
        response.setContentLength((int) file.length());
        ServletOutputStream out = null;
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }

    }

}
