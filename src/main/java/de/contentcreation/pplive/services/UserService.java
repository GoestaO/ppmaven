package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import de.contentcreation.pplive.model.Rolle;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    User user;

    public User login(String username, String password) {

        Query q = em
                .createQuery("select u from User u where u.nick = :username and u.passwort = :password");
        q.setParameter("username", username);
        q.setParameter("password", password);
        try {
            user = (User) q.getSingleResult();
            if (user != null) {
                String vorname = user.getVorname();
                String nachname = user.getNachname();
                Rolle rolle = user.getRolle();
                user.setVorname(vorname);
                user.setNachname(nachname);
                user.setRolle(rolle);
                user.setValid(true);
            }
        } catch (NoResultException nrex) {
            user = null;
        }
        return user;
    }
}
