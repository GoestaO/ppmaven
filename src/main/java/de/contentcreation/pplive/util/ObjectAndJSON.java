/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.util;

import com.google.gson.Gson;
import de.contentcreation.pplive.model.BacklogArticle;

/**
 * Diese Klasse enthält Methoden, die dazu dienen, aus einem komplexen Objekt
 * ein JSON-Objekt zu erzeugen und umgekehrt
 *
 * @author dwagner
 */
public class ObjectAndJSON {

    public static BacklogArticle fromJSON(String message) {
        Gson gson = new Gson();

        BacklogArticle p = gson.fromJson(message, BacklogArticle.class);
        return p;
    }

    public static String toJSON(BacklogArticle p) {
        Gson gson = new Gson();
        String s = gson.toJson(p);

        return s;
    }

}
