package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.BacklogArticle;
import de.contentcreation.pplive.model.Rolle;
import de.contentcreation.pplive.model.UpdateBuchung;
import de.contentcreation.pplive.model.User;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.Bemerkung;


@Stateless
public class DatabaseHandler {

    @PersistenceContext
    private EntityManager em;

    public List<BacklogArticle> getBacklog() {

        TypedQuery<BacklogArticle> showBacklog = em.createQuery(
                "select b from BacklogArticle b where b.offen = '1' ",
                BacklogArticle.class);

        List<BacklogArticle> backlogList = showBacklog.getResultList();       

        return backlogList;
    }

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

    public List<BacklogArticle> getBacklogByPartner2(List<Integer> partnerList) {
        
        TypedQuery<BacklogArticle> query = em
                .createQuery(
                        "select b from BacklogArticle b where b.offen = '1' and b.partnerId in :partnerList",
                        BacklogArticle.class);
        query.setParameter("partnerList", partnerList);
        List<BacklogArticle> backlogList = query.getResultList();
       
        return backlogList;
    }

    public int getBacklogSize() {
        
        TypedQuery<BacklogArticle> showBacklog = em.createQuery(
                "select b from BacklogArticle b where b.offen = '1' ",
                BacklogArticle.class);
        List<BacklogArticle> backlogList = showBacklog.getResultList();
        
        return backlogList.size();
    }

    public BacklogArticle getBacklogArticle(String identifier) {
        
        BacklogArticle backlogArticle = (BacklogArticle) em.find(
                BacklogArticle.class, identifier);
        em.close();
       
        return backlogArticle;
    }

    public int checkAndAdd(List<BacklogArticle> backlogList) {
        int counter = 0;
       
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
      
        return counter;
    }

    public void updateArticleStatus(String identifier, String bemerkung1,
            String bemerkung2, String bemerkung3, String bemerkungKAM,
            boolean neuerStatus, User currentUser, String season) {
     
        BacklogArticle ba = (BacklogArticle) em.find(BacklogArticle.class,
                identifier);
        UpdateBuchung updateBuchung = new UpdateBuchung();
        
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
        updateBuchung.setStatus(neuerStatus);
        updateBuchung.setSaison(season);

        em.merge(ba);
        em.persist(updateBuchung);
        
    }

    public String registerUser(String nick, String vorname, String nachname,
            String passwort) {
       
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
        
        return forwardPage;
    }

    public void setCounter() {
        
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
       
    }

    public List<String> getSeasons() {
        
        TypedQuery<String> getSeasons = em
                .createQuery(
                        "select distinct(b.saison) from BacklogArticle b where b.saison !='' order by b.saison",
                        String.class);
        List<String> seasons = getSeasons.getResultList();
        em.close();
       
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
        
        TypedQuery<Integer> getPartnerQuery = em
                .createQuery(
                        "Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 and b.offen = 1 order by b.partnerId",
                        Integer.class);
        List<Integer> partner = getPartnerQuery.getResultList();
        em.close();
       
        return partner;
    }

    public List<Integer> getPartner2() {
       
        TypedQuery<Integer> getPartnerQuery = em
                .createQuery(
                        "Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 order by b.partnerId",
                        Integer.class);
        List<Integer> partner = getPartnerQuery.getResultList();
        em.close();
       

        return partner;
    }

    public List<String> getPartnerListString() {
        List<Integer> partnerList = this.getPartner();

        List<String> partnerListString = new ArrayList();
        for (Iterator localIterator = partnerList.iterator(); localIterator
                .hasNext();) {
            int i = ((Integer) localIterator.next()).intValue();
            partnerListString.add(Integer.toString(i));
        }
        return partnerListString;
    }

    public List<Bemerkung> getBemerkungen() {       
        
        TypedQuery<Bemerkung> getBemerkungQuery = em.createQuery(
                "Select b from Bemerkung b", Bemerkung.class);
        List<Bemerkung> bemerkungen = getBemerkungQuery.getResultList();
        
        return bemerkungen;
    }
}
