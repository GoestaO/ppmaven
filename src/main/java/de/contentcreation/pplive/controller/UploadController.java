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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */
@Named
@RequestScoped
public class UploadController {

    @EJB
    private DatabaseHandler dh;

    int counter = 0;

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        List<BacklogArticle> backlogList = new ArrayList();
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
            FileReader reader = new FileReader(targetFile);
            BufferedReader br = new BufferedReader(reader);
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] data = line.split(";");
                String config = data[0];
                int partnerID = Integer.parseInt(data[1]);
                String warengruppenpfad = data[7];
                String saison = data[8];
                int appdomainID = Integer.parseInt(data[9]);
                String identifier = config + partnerID + appdomainID;
                BacklogArticle ba = new BacklogArticle();
                ba.setIdentifier(identifier);
                ba.setAppdomainId(appdomainID);
                ba.setConfig(config);
                ba.setPartnerId(partnerID);
                ba.setCgPath(warengruppenpfad);
                ba.setSaison(saison);
                ba.setDatum(Calendar.getInstance().getTime());
                ba.setOffen(true);
                backlogList.add(ba);
                line = br.readLine();                
            }
            br.close();
            counter = dh.checkAndAdd(backlogList);
            System.out.println("beendet.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesMessage message = new FacesMessage("Upload", counter + " Artikel erfolgreich hochgeladen.");
        FacesContext.getCurrentInstance()
                .addMessage(null, message);
    }
}
