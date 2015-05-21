/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.converter;

import de.contentcreation.pplive.model.User;
import de.contentcreation.pplive.services.UserService;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author gostendorf
 */
@FacesConverter("userConverter")
public class UserConverter implements Converter {

    @EJB
    private UserService us;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        User user = null;
        int id = Integer.parseInt(value);
        List<User> userList = us.findAll();

        for (User u : userList) {
            if (u.getId() == id) {
                user = u;
            }
        }
        return user;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof User) {
            return String.valueOf(((User) value).getId());
        } else {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid User", value)));
        }
    }

}
