package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import de.contentcreation.pplive.model.Rolle;
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

    public void registerUser(String nick, String vorname, String nachname,
            String passwort) {
        TypedQuery<User> checkForNick = em.createQuery(
                "select u from User u where u.nick = :nick", User.class);
        checkForNick.setParameter("nick", nick);

        List<User> userList = checkForNick.getResultList();
        Rolle rolle = new Rolle();
        rolle.setId(4);
        if (!userList.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nutzername bekannt", "Dieser Nutzername ist bereits vergeben, bitte versuche einen anderen!");
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
        } else if (userList.isEmpty()) {
            User u = new User();
            u.setNick(nick);
            u.setVorname(vorname);
            u.setNachname(nachname);
            u.setPasswort(passwort);
            u.setRolle(rolle);
            em.persist(user);
        }
        em.close();

    }
}
