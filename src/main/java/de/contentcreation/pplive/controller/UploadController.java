/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.services.DatabaseHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;

/**
 * Diese Datei ist der Controller für die Upload-Seite und sorgt dafür, dass
 * eine Backlog-Datei in die DB eingespielt wird.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Named
@RequestScoped
public class UploadController implements Serializable {

    long ean;

    @EJB
    private DatabaseHandler dh;

    int counter = 0;

    /**
     * Diese Methode sorgt dafür, dass eine gewählte Datei auf den Server
     * geladen wird. Hierzu wird zunächst die hochgeladene CSV geparst und aus
     * den entsprechenden Spalten ein BacklogArticle-Objekt erzeugt wird. Diese
     * Objekte werden dann in einer Liste gespeichert. Anschließend wird die
     * Objekte in der Liste mit der Datenbank abgeglichen, ob sie schon
     * vorhanden sind oder nicht. Noch nicht vorhandene Objekte werden dann in
     * der Datenbank abgespeichert.
     *
     * @param event Das FileUpload-Event
     */
    public void handleFileUpload(FileUploadEvent event) {
        List<BacklogArticle> backlogList = new ArrayList();
        
        // Die hochzuladende Datei auf den Server laden und in backlog.csv abspeichern
        try {
            File targetFile = new File("backlog.csv");
            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFile.getName()));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            
            // Anschließend die gerade hochgeladene Datei einlesen und parsen
            FileReader reader = new FileReader(targetFile);
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] data = line.split(";", -1);
                String config = data[0];
                try {
                    ean = Long.parseLong(data[9]);
                } catch (NumberFormatException ex) {
                    ean = 0;
                }
                int partnerID = Integer.parseInt(data[1]);
                String warengruppenpfad = data[7];
                String saison = data[8];
                int appdomainID = Integer.parseInt(data[10]);
                String identifier = config + partnerID + appdomainID;
                
                // BacklogArticle-Objekt erzeugen und die Attribute setzen
                BacklogArticle ba = new BacklogArticle();
                ba.setIdentifier(identifier);
                ba.setAppdomainId(appdomainID);
                ba.setConfig(config);
                ba.setPartnerId(partnerID);
                ba.setCgPath(warengruppenpfad);
                ba.setSaison(saison);
                ba.setEan(ean);
                ba.setDatum(Calendar.getInstance().getTime());
                ba.setOffen(true);
                backlogList.add(ba);
                line = br.readLine();
            }
            br.close();
            
            // Die Liste an den DB-Handler übergeben, der jedes Objekt auf Vorhandensein überprüft und eventuell abspeichert
            counter = dh.checkAndAdd(backlogList);
        } catch (IOException e) {
            Logger.getLogger(UploadController.class.getClass()).log(Logger.Level.FATAL, e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Upload fehlgeschlagen", e.getMessage());
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
        }
        FacesMessage message = new FacesMessage("Upload", counter + " Artikel erfolgreich hochgeladen.");
        FacesContext.getCurrentInstance()
                .addMessage(null, message);
    }
}
