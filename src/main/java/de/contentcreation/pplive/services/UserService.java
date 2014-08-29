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
import javax.persistence.TypedQuery;

/**
 * Diese Klasse ist für alle Datenbankabfragen, die den Nutzer betreffen,
 * zuständig.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    User user;

    /**
     * Sucht ein Userobjekt mit Hilfe des Nutzernamens und des Passworts, wenn
     * ein Eintrag mit dieser Konstellation gefunden wurde, wird das Userobjekt
     * zurückgegeben.
     *
     * @param username Der Nutzername
     * @param password Das Passwort
     * @return
     */
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

    /**
     * Diese Methode speichert einen Nutzer, der sich neu registrieren möchte.
     * Zunächst wird geprüft, ob der Nutzername schon vergeben ist, dann wird
     * der Rückgabewert false ausgegeben. Ansonsten wird der Nutzer angelegt und
     * abgespeichert sowie true zurückgegeben.
     *
     * @param nick Der Nutzername
     * @param vorname Der Vorname
     * @param nachname Der Nachname
     * @param passwort Das Passwort
     * @return Registrierung war erfolgreich; true = ja, false = nein
     */
    public boolean registerUser(String nick, String vorname, String nachname,
            String passwort) {

        boolean returnValue = false;

        TypedQuery<User> checkForNick = em.createQuery(
                "select u from User u where u.nick = :nick", User.class);
        checkForNick.setParameter("nick", nick);
        List<User> userList = checkForNick.getResultList();
        if (userList.size() == 0) {
            User u = new User();
            Rolle rolle = new Rolle();
            rolle.setId(4);
            u.setNick(nick);
            u.setVorname(vorname);
            u.setNachname(nachname);
            u.setPasswort(passwort);
            u.setRolle(rolle);
            em.persist(u);
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Verschlüsselt einen Input in einen MD5-Hash
     * @param input Der Input
     * @return Der MD5-Hash
     */
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
