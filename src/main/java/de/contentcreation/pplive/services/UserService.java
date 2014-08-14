package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import de.contentcreation.pplive.model.Rolle;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.TypedQuery;

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

    public boolean registerUser(String nick, String vorname, String nachname,
            String passwort) {

        
        boolean returnValue = false;

        TypedQuery<User> checkForNick = em.createQuery(
                "select u from User u where u.nick = :nick", User.class);
        checkForNick.setParameter("nick", nick);

        List<User> userList = checkForNick.getResultList();
        Rolle rolle = new Rolle();
        rolle.setId(4);
    if (userList.isEmpty()) {
            User u = new User();
            u.setNick(nick);
            u.setVorname(vorname);
            u.setNachname(nachname);
            u.setPasswort(passwort);
            u.setRolle(rolle);
            em.persist(user);
            returnValue = true;
        }
        em.close();
        return returnValue;
    }

    private String md5(String input) {
        String md5 = null;
        if (input == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(input.getBytes(), 0, input.length());

            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
