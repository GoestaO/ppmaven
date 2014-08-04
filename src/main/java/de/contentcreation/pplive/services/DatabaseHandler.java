package de.contentcreation.pplive.services;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.BacklogArticle;
import model.Bemerkung;
import model.Rolle;
import model.UpdateBuchung;
import model.User;

public class DatabaseHandler {
	private static DatabaseHandler instance = null;

	public static DatabaseHandler getInstance() {
		if (instance == null) {
			instance = new DatabaseHandler();
		}
		return instance;
	}

	public List<BacklogArticle> getBacklog() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();

		TypedQuery<BacklogArticle> showBacklog = em.createQuery(
				"select b from BacklogArticle b where b.offen = '1' ",
				BacklogArticle.class);

		List<BacklogArticle> backlogList = showBacklog.getResultList();
		em.close();
		factory.close();

		return backlogList;
	}

	public List<Object[]> getBacklogByPartner(List<Integer> partnerList) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();

		StringBuilder builder = new StringBuilder();

		builder.append("(");
		for (Integer partner : partnerList) {
			builder.append(partner);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);

		builder.append(")");
		String listParameter = builder.toString();

		String abfrage = "SELECT gesamt.*, COUNT(gesamt.Config) FROM backlog gesamt left join (Select backlog.Config as c from backlog where backlog.OFFEN = 1) b on gesamt.Config = b.c where gesamt.OFFEN = 1 and gesamt.PartnerId in"
				+ listParameter + " group by gesamt.Identifier ORDER BY NULL";

		Query query = em.createNativeQuery(abfrage);

		List<Object[]> backlogList = query.getResultList();

		em.close();
		factory.close();
		return backlogList;
	}

	public List<BacklogArticle> getBacklogByPartner2(List<Integer> partnerList) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<BacklogArticle> query = em
				.createQuery(
						"select b from BacklogArticle b where b.offen = '1' and b.partnerId in :partnerList",
						BacklogArticle.class);
		query.setParameter("partnerList", partnerList);
		List<BacklogArticle> backlogList = query.getResultList();
		em.close();
		factory.close();
		return backlogList;
	}

	public int getBacklogSize() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<BacklogArticle> showBacklog = em.createQuery(
				"select b from BacklogArticle b where b.offen = '1' ",
				BacklogArticle.class);
		List<BacklogArticle> backlogList = showBacklog.getResultList();
		em.close();
		factory.close();
		return backlogList.size();
	}

	public BacklogArticle getBacklogArticle(String identifier) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		BacklogArticle backlogArticle = (BacklogArticle) em.find(
				BacklogArticle.class, identifier);
		em.close();
		factory.close();
		return backlogArticle;
	}

	public int checkAndAdd(List<BacklogArticle> backlogList) {
		int counter = 0;
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		for (BacklogArticle ba : backlogList) {
			BacklogArticle checkedArticle = (BacklogArticle) em.find(
					BacklogArticle.class, ba.getIdentifier());
			if (checkedArticle == null) {
				em.getTransaction().begin();
				em.persist(ba);
				em.getTransaction().commit();
				counter++;
			}
		}
		em.close();
		factory.close();
		return counter;
	}

	public void updateArticleStatus(String identifier, String bemerkung1,
			String bemerkung2, String bemerkung3, String bemerkungKAM,
			String neuerStatus, User currentUser, String season) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();

		BacklogArticle ba = (BacklogArticle) em.find(BacklogArticle.class,
				identifier);

		em.getTransaction().begin();

		UpdateBuchung updateBuchung = new UpdateBuchung();
		if (neuerStatus.equals("fertig")) {
			ba.setOffen(false);
		}
		ba.setBemerkung1(bemerkung1);
		ba.setBemerkung2(bemerkung2);
		ba.setBemerkung3(bemerkung3);
		ba.setBemerkungKAM(bemerkungKAM);
		ba.setSaison(season);

		updateBuchung.setBacklogArticle(ba);
		updateBuchung.setUser(currentUser);
		Calendar now = Calendar.getInstance();
		updateBuchung.setTimestamp(now.getTime());
		updateBuchung.setBemerkung1(bemerkung1);
		updateBuchung.setBemerkung2(bemerkung2);
		updateBuchung.setBemerkung3(bemerkung3);
		updateBuchung.setBemerkungKAM(bemerkungKAM);
		updateBuchung.setStatus(neuerStatus);
		updateBuchung.setSaison(season);

		em.persist(ba);
		em.persist(updateBuchung);
		em.getTransaction().commit();
		em.close();
		factory.close();
	}

	public String registerUser(String nick, String vorname, String nachname,
			String passwort) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		String forwardPage = null;

		TypedQuery<User> checkForNick = em.createQuery(
				"select u from User u where u.nick = :nick", User.class);
		checkForNick.setParameter("nick", nick);

		List<User> userList = checkForNick.getResultList();
		Rolle rolle = new Rolle();
		rolle.setId(4);
		if (userList.size() > 0) {
			forwardPage = "/invalidNickname.jsp";
		} else if (userList.size() == 0) {
			em.getTransaction().begin();
			User user = new User();
			user.setNick(nick);
			user.setVorname(vorname);
			user.setNachname(nachname);
			user.setPasswort(passwort);
			user.setRolle(rolle);
			em.persist(user);
			em.getTransaction().commit();
			forwardPage = "/confirmRegistration.jsp";
		}
		em.close();
		factory.close();
		return forwardPage;
	}

	public void setCounter() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		System.out.println("Starte das Zählen");
		long start = -System.currentTimeMillis();

		em.getTransaction().begin();
		List<BacklogArticle> backlogList = getBacklog();
		for (BacklogArticle backlogArticle : backlogList) {
			TypedQuery<Long> nrSKUs = em
					.createQuery(
							"SELECT count(b.config) FROM BacklogArticle b where b.offen = 1 and b.config = :sku",
							Long.class);

			nrSKUs.setParameter("sku", backlogArticle.getConfig());
			long count = ((Long) nrSKUs.getSingleResult()).longValue();
			System.out.println(count);
			backlogArticle.setCounter((int) count);
			em.merge(backlogArticle);
		}
		em.getTransaction().commit();
		em.close();
		factory.close();
		System.out
				.println((start + System.currentTimeMillis()) / 1000L
						+ "s für das Zählen von " + backlogList.size()
						+ " Artikeln.");
	}

	public List<String> getSeasons() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<String> getSeasons = em
				.createQuery(
						"select distinct(b.saison) from BacklogArticle b where b.saison !='' order by b.saison",
						String.class);
		List<String> seasons = getSeasons.getResultList();
		em.close();
		factory.close();
		return seasons;
	}

	public String md5(String input) {
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

	public List<Integer> getPartner() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<Integer> getPartnerQuery = em
				.createQuery(
						"Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 and b.offen = 1 order by b.partnerId",
						Integer.class);
		List<Integer> partner = getPartnerQuery.getResultList();
		em.close();
		factory.close();
		return partner;
	}

	public List<Integer> getPartner2() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<Integer> getPartnerQuery = em
				.createQuery(
						"Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 order by b.partnerId",
						Integer.class);
		List<Integer> partner = getPartnerQuery.getResultList();
		em.close();
		factory.close();

		return partner;
	}

	public List<String> getPartnerListString() {
		List<Integer> partnerList = getPartner();

		List<String> partnerListString = new ArrayList();
		for (Iterator localIterator = partnerList.iterator(); localIterator
				.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();
			partnerListString.add(Integer.toString(i));
		}
		return partnerListString;
	}

	public List<Bemerkung> getBemerkungen() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("PartnerprogrammAlternative");
		EntityManager em = factory.createEntityManager();
		TypedQuery<Bemerkung> getBemerkungQuery = em.createQuery(
				"Select b from Bemerkung b", Bemerkung.class);
		List<Bemerkung> bemerkungen = getBemerkungQuery.getResultList();
		em.close();
		factory.close();
		return bemerkungen;
	}
}
