/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.contentcreation.pplive.converter;

import de.contentcreation.pplive.services.DatabaseHandler;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Bemerkung;



/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */

@FacesConverter(value="bemerkungsConverter", forClass = Bemerkung.class)
public class BemerkungsConverter implements Converter{

    @EJB
    private DatabaseHandler dh;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
