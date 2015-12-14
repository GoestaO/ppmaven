/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.converter;

import de.contentcreation.pplive.model.Rolle;
import de.contentcreation.pplive.services.DatabaseHandler;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

/**
 *
 * @author goesta
 */
@Named
public class RollenConverter implements Converter {

    @EJB
    private DatabaseHandler handler;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Integer id = Integer.parseInt(value);
        Rolle r = handler.findRoleById(id);
        return r;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Rolle) {
            Rolle r = (Rolle) value;
            return String.valueOf(r.getId());
        }
        return null;
    }

}
