/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.contentcreation.pplive.controller;

import de.contentcreation.pplive.services.UserService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author goesta
 */

@Named
@RequestScoped
public class RegisterController {
    
    @EJB
    private UserService service;
    
}
