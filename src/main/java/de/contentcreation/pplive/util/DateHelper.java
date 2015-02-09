package de.contentcreation.pplive.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 * Klasse, die Konvertierungen zwischen Datum und String vornimmt.
 */
public final class DateHelper {

    public static String getFirstDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String firstDateString = df.format(date);
        return firstDateString;
    }

    public static String getSecondDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String secondDateString = df.format(date);
        return secondDateString;
    }

    public static void showDateSelectedMessage(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Datum ausgewählt", format.format(event.getObject()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public static String dateToDatumString(Date datum) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return (datum != null) ? format.format(datum) : "";
    }
}
