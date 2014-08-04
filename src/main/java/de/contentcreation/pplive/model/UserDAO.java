package model;

import java.io.PrintStream;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UserDAO {
	static ResultSet rs = null;
	static EntityManagerFactory factory;

	public static User login(User user) {
		String nick = user.getNick();
		String passwort = user.getPasswort();
		factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		Query q = em
				.createQuery("select u from User u where u.nick = :nick and u.passwort = :passwort");
		q.setParameter("nick", nick);
		q.setParameter("passwort", passwort);
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
			} else {
				System.out
						.println("Sorry, you are not a registered user! Please sign up first");
				user.setValid(false);
			}
		} catch (NoResultException nrex) {
			user.setValid(false);
		}
		em.close();
		return user;
	}
}
