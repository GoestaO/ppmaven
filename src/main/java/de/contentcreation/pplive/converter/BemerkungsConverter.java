/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.contentcreation.pplive.converter;

import de.contentcreation.pplive.model.Bemerkung;
import de.contentcreation.pplive.services.DatabaseHandler;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



/**
 * Die Implementierung des Bemerkungsconverters
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */

@FacesConverter(value="bemerkungsConverter", forClass = Bemerkung.class)
public class BemerkungsConverter implements Converter{

    @EJB
    private DatabaseHandler dh;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Long id = dh.getBemerkungId(value);
        Bemerkung b = new Bemerkung();
        b.setID(id);
        b.setBemerkung(value);
        return b;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
    
}
