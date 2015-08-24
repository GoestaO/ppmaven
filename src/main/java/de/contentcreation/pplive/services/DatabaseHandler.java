package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.Partner;
import de.contentcreation.pplive.model.UpdateBuchung;
import de.contentcreation.pplive.model.User;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Die Implemntierung des Datenbank-Handlers. Der Datenbank-Handler ist
 * zuständig für alle Datenbank-Abfragen, die Artikel betreffen.
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Stateless
public class DatabaseHandler {

    @PersistenceContext
    private EntityManager em;

    /**
     * Gibt alle offenen Backlog-Artikel aus
     *
     * @return Liste mit den Backlog-Artikeln
     */
    public List<BacklogArticle> getBacklog() {

        TypedQuery<BacklogArticle> showBacklog = em.createQuery(
                "select b from BacklogArticle b where b.offen = '1' ",
                BacklogArticle.class);

        List<BacklogArticle> backlogList = showBacklog.getResultList();

        return backlogList;
    }

    /**
     * Gibt alle offenen Backlog-Artikel aus, die von einer Liste gewisser
     * Partnern stammen.
     *
     * @param partnerList
     * @return Liste mit Backlog-Artikeln
     */
    public List<Object[]> getBacklogByPartner(List<Integer> partnerList) {

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

        return backlogList;
    }

    /**
     * Gibt alle offenen Backlog-Artikel aus, die von einer Liste gewisser
     * Partnern stammen.
     *
     * @param partnerList
     * @return Liste mit Backlog-Artikeln
     */
    public List<BacklogArticle> getBacklogByPartner2(List<Integer> partnerList) {

        TypedQuery<BacklogArticle> query = em
                .createQuery(
                        "select b from BacklogArticle b where b.offen = 1 and b.partnerId in :partnerList order by b.partnerId",
                        BacklogArticle.class);
        query.setParameter("partnerList", partnerList);
        List<BacklogArticle> backlogList = query.getResultList();

        return backlogList;
    }

    /**
     * Gibt die Anzahl der offenen Backlog-Artikel aus.
     *
     * @return Anzahl der offenen Artikel
     */
    public int getBacklogSize() {

        TypedQuery<BacklogArticle> showBacklog = em.createQuery(
                "select b from BacklogArticle b where b.offen = '1' ",
                BacklogArticle.class);
        List<BacklogArticle> backlogList = showBacklog.getResultList();

        return backlogList.size();
    }

    /**
     * Gibt einen bestimmten Backlog-Artikel aus
     *
     * @param identifier Der Identifier
     * @return Der Backlog-Artikel
     */
    public BacklogArticle getBacklogArticle(String identifier) {

        BacklogArticle backlogArticle = (BacklogArticle) em.find(
                BacklogArticle.class, identifier);

        return backlogArticle;
    }

    /**
     * Iteriert eine Liste von Backlog-Artikeln und untersucht jeden dort drin
     * befindlichen Artikel auf bereits Vorhandensein in der Datenbank. Wenn der
     * Artikel noch nicht vorhanden ist, wird er gespeichert, ansonsten nicht.
     * Gibt die Anzahl der hinzugefügten Artikel aus.
     *
     * @param backlogList
     * @return Anzahl der hinzugefügten neuen Artikel
     */
    public int checkAndAdd(List<BacklogArticle> backlogList) {
        int counter = 0;

        for (BacklogArticle ba : backlogList) {
            BacklogArticle checkedArticle = (BacklogArticle) em.find(
                    BacklogArticle.class, ba.getIdentifier());
            if (checkedArticle == null) {
                em.persist(ba);
                counter++;
            }
        }

        return counter;
    }

    /**
     * Aktualisiert den Status eines Artikels und erzeugt eine Buchung mit den
     * gemachten Angaben.
     *
     * @param identifier Der Identifier
     * @param bemerkung1 Bemerkung 1
     * @param bemerkung2 Bemerkung 2
     * @param bemerkung3 Bemerkung 3
     * @param bemerkungKAM Bemerkung KAM
     * @param neuerStatus Der neue Status: offen oder fertig
     * @param currentUser Der Nutzer, der den Artikel bearbeitet hat
     * @param season Die Saison
     */
    public void updateArticleStatus(String identifier, String bemerkung1,
            String bemerkung2, String bemerkung3, String bemerkungKAM,
            boolean neuerStatus, User currentUser, String season) {

        BacklogArticle ba = (BacklogArticle) em.find(BacklogArticle.class,
                identifier);
        UpdateBuchung updateBuchung = new UpdateBuchung();
        String neuerStatusString = "";
        if (neuerStatus) {
            neuerStatusString = "offen";
        } else {
            neuerStatusString = "fertig";
        }

        ba.setOffen(neuerStatus);
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
        updateBuchung.setStatus(neuerStatusString);
        updateBuchung.setSaison(season);

        em.merge(ba);
        em.persist(updateBuchung);

    }

    /**
     * Gibt alle Saisons einmalig aus, die in der DB vorhanden sind.
     *
     * @return
     */
    public List<String> getSeasons() {

        TypedQuery<String> getSeasons = em
                .createQuery(
                        "select distinct(b.saison) from BacklogArticle b where b.saison !='' order by b.saison",
                        String.class);
        List<String> seasons = getSeasons.getResultList();

        return seasons;
    }

    /**
     * Verschlüsselt einen String in einen MD5-Hash
     *
     * @param input Der Input
     * @return Der MD5-Hash
     */
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

    /**
     * Gibt alle Partner-IDs aus, von denen noch Artikel offen sind.
     *
     * @return Liste mit den Partnern
     */
    public List<Integer> getPartner() {

        TypedQuery<Integer> getPartnerQuery = em
                .createQuery(
                        "Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 and b.offen = 1 order by b.partnerId",
                        Integer.class);
        List<Integer> partner = getPartnerQuery.getResultList();

        return partner;
    }

    /**
     * Gibt alle Partner-IDs aus, auch von Artikeln, die bereits geschlossen
     * wurden.
     *
     * @return Liste mit den Partnern
     */
    public List<Integer> getPartner2() {

        TypedQuery<Integer> getPartnerQuery = em
                .createQuery(
                        "Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 order by b.partnerId",
                        Integer.class);
        List<Integer> partner = getPartnerQuery.getResultList();

        return partner;
    }

    /**
     * Gibt alle Partner-Objekte aus
     *
     * @return
     */
    public List<Partner> getPartnerLoginScreen() {
        TypedQuery<Partner> q = em.createNamedQuery("Partner.findAllInBacklog", Partner.class);
        List<Partner> result = q.getResultList();
        return result;
    }

    /**
     * Gibt alle Bemerkungen1 aus
     *
     * @return Die Bemerkungen
     */
    public List<String> getBemerkungen1() {

        TypedQuery<String> getBemerkungQuery = em.createQuery(
                "Select distinct b.bemerkung from Bemerkung b where b.typ = 1", String.class);
        List<String> bemerkungen = getBemerkungQuery.getResultList();

        return bemerkungen;
    }

    /**
     * Gibt alle Bemerkungen2 aus
     *
     * @return Die Bemerkungen
     */
    public List<String> getBemerkungen2() {

        TypedQuery<String> getBemerkungQuery = em.createQuery(
                "Select distinct b.bemerkung from Bemerkung b where b.typ = 2", String.class);
        List<String> bemerkungen = getBemerkungQuery.getResultList();

        return bemerkungen;
    }

    /**
     * Gibt die ID (Key) einer bestimmten Bemerkung aus
     *
     * @param value Die gesuchte Bemerkung
     * @return Die gefundene entsprechende ID.
     */
    public Long getBemerkungId(String value) {
        Long id;
        TypedQuery<Long> query = em.createQuery("Select b.id from Bemerkung b where b.bemerkung = :value", Long.class);
//        query.setParameter(value, value);
        query.setParameter("value", value);
        try {
            id = query.getSingleResult();
        } catch (NoResultException ex) {
            id = null;
        }
        return id;
    }

    public void insertPartners(List<Partner> list) {
        for (Partner p : list) {
            em.persist(p);
            em.flush();
        }
    }

    public void clearPartnerTable() {
        em.createQuery("Delete from Partner p").executeUpdate();
    }

    public boolean unknownPartnersFound() {
        TypedQuery<Long> query = em.createQuery("select count(b) from BacklogArticle b  where b.partnerId > 0 and b.offen = 1 and b.partnerId not in (select p.id from Partner p)", Long.class);
        Long result = query.getSingleResult();
//        System.out.println("result = " + result);
        if (result > 0) {
            return true;
        }
        return false;
    }
    
    public List<Integer> getUnknownPartners(){
        TypedQuery<Integer> query = em.createQuery("select distinct(b.partnerId) from BacklogArticle b  where b.partnerId > 0 and b.offen = 1 and b.partnerId not in (select p.id from Partner p)", Integer.class);
        List<Integer> result = query.getResultList();
        return result;
    }

}
