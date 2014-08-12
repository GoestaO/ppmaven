package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.BacklogArticle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import de.contentcreation.pplive.reportingClasses.PartnerReport;
import de.contentcreation.pplive.reportingClasses.UserReport;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

@Stateless
public class ReportingHandler {

    @PersistenceContext
    private EntityManager em;

    public List<BacklogArticle> getOpenBacklogArticles() {

        TypedQuery<BacklogArticle> query = em
                .createQuery(
                        "Select b from BacklogArticle b where (b.bemerkung1 IS NULL or b.bemerkung1 =' ') AND b.offen = 1 order by b.datum, b.partnerId",
                        BacklogArticle.class);
        List<BacklogArticle> newBacklogArticles = query.getResultList();

        return newBacklogArticles;
    }

    public List<BacklogArticle> getNewBacklogArticles() {

        TypedQuery<BacklogArticle> query = em
                .createQuery(
                        "Select b from BacklogArticle b where b.datum is not null order by b.datum, b.partnerId",
                        BacklogArticle.class);

        List<BacklogArticle> newBacklogArticles = query.getResultList();

        return newBacklogArticles;
    }

    public List<UserReport> getUserReport(Date datum1, Date datum2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        TypedQuery<UserReport> query = em
                .createQuery(
                        "Select new de.contentcreation.pplive.reportingClasses.UserReport(u.vorname, u.nachname, art.appdomainId, art.config, art.partnerId, art.cgPath, art.saison, ub.timestamp, ub.status, ub.bemerkung1, ub.bemerkung2, ub.bemerkung3, ub.bemerkungKAM) from UpdateBuchung ub left join ub.user u left join ub.backlogArticle art where ub.timestamp > :datum1 and ub.timestamp < :datum2",
                        UserReport.class);
        System.out.println("query = " + query);
        query.setParameter("datum1", datum1);
        query.setParameter("datum2", datum2);
        List<UserReport> buchungsListe = query.getResultList();

        return buchungsListe;
    }

    public List<PartnerReport> getPartnerReport() {

        TypedQuery<PartnerReport> query = em
                .createQuery(
                        "Select new reportingClasses.PartnerReport(b.partnerId, b.config, b.appdomainId, b.cgPath, b.saison, b.bemerkung1, b.bemerkung2, b.bemerkung3, b.bemerkungKAM, b.offen) from BacklogArticle b where b.offen = 1 order by b.partnerId",
                        PartnerReport.class);
        List<PartnerReport> resultList = query.getResultList();

        return resultList;
    }

    // public List<PartnerReport> getProblemArticles() {
    // EntityManagerFactory ef = Persistence
    // .createEntityManagerFactory("PartnerprogrammAlternative");
    // EntityManager em = ef.createEntityManager();
    // TypedQuery<PartnerReport> query = em
    // .createQuery(
    // "Select new reportingClasses.PartnerReport(b.partnerId, b.config, b.appdomainId, b.cgPath, b.saison, b.bemerkung1, b.bemerkung2, b.bemerkung3, b.bemerkungKAM, b.offen) from BacklogArticle b where b.bemerkung1 not like '' or b.bemerkung2 not like '' or b.bemerkung3 not like '' order by b.partnerId",
    // PartnerReport.class);
    // List<PartnerReport> resultList = query.getResultList();
    // em.close();
    // ef.close();
    // return resultList;
    // }
    public List<Object[]> getProblemArticles() {

        Query query = em
                .createNativeQuery("select distinct(b.Identifier), b.PartnerID, b.Config, b.AppdomainID, b.Warengruppenpfad, b.Saison, user.VORNAME, user.NACHNAME, b.Bemerkung1, b.Bemerkung2, b.Bemerkung3, b.BemerkungKAM, b.OFFEN \n "
                        + "from backlog b \n"
                        + "left join (select max(buchungen.Timestamp), buchungen.Identifier, buchungen.User, buchungen.Bemerkung1, buchungen.Bemerkung2, buchungen.Bemerkung3, buchungen.BemerkungKAM from buchungen group by buchungen.Identifier) c on b.Identifier = c.Identifier \n"
                        + "inner join user on user.ID = c.User \n"
                        + "where b.Bemerkung1 not like '' or b.Bemerkung2 not like '' or b.Bemerkung3 not like '' \n"
                        + "and b.Bemerkung1 = c.Bemerkung1 and b.Bemerkung2 = c.Bemerkung2 and b.Bemerkung3 = c.Bemerkung3 and b.BemerkungKAM = c.BemerkungKAM \n"
                        + "order by b.PartnerID");
        List<Object[]> resultList = query.getResultList();

        return resultList;
    }

    public List<Object[]> getEditedDataByPartner(Integer partner) {

        String q = "Select backlog.Identifier, backlog.PartnerID, backlog.Config, backlog.AppdomainID, backlog.Warengruppenpfad, backlog.Saison, max(buchungen.Timestamp), backlog.Bemerkung1, backlog.Bemerkung2, backlog.Bemerkung3, backlog.BemerkungKAM from backlog left join buchungen on backlog.Identifier = buchungen.Identifier where backlog.PartnerID = "
                + Integer.toString(partner.intValue())
                + " and backlog.OFFEN = 0 group by backlog.Identifier";
        Query query = em.createNativeQuery(q);

        List<Object[]> resultList = query.getResultList();

        return resultList;
    }
}
