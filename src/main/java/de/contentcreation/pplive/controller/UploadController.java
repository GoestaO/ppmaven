/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesMessage message = new FacesMessage("Upload", file.getFileName() + " erfolgreich hochgeladen.");
        FacesContext.getCurrentInstance()
                .addMessage(null, message);
    }
}
