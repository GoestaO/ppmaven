package de.contentcreation.pplive.services;

import de.contentcreation.pplive.model.BacklogArticle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import de.contentcreation.pplive.reportingClasses.PartnerReport;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung1;
import de.contentcreation.pplive.reportingClasses.RejectReportBemerkung2;
import de.contentcreation.pplive.reportingClasses.RejectReportOverview;
import de.contentcreation.pplive.reportingClasses.UserReport;
import de.contentcreation.pplive.util.QueryHelper;
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

    public List<BacklogArticle> getNewBacklogArticles(Date datum1, Date datum2) {

        TypedQuery<BacklogArticle> query = em
                .createQuery(
                        "Select b from BacklogArticle b where b.datum is not null and b.datum between :datum1 and :datum2 order by b.datum, b.partnerId",
                        BacklogArticle.class);
        query.setParameter("datum1", datum1);
        query.setParameter("datum2", datum2);

        List<BacklogArticle> newBacklogArticles = query.getResultList();

        return newBacklogArticles;
    }

    public List<UserReport> getUserReport(Date datum1, Date datum2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        TypedQuery<UserReport> query = em
                .createQuery(
                        "Select new de.contentcreation.pplive.reportingClasses.UserReport(u.vorname, u.nachname, art.appdomainId, art.config, art.partnerId, art.cgPath, ub.saison, ub.timestamp, ub.status, ub.bemerkung1, ub.bemerkung2, ub.bemerkung3, ub.bemerkungKAM) from UpdateBuchung ub left join ub.user u left join ub.backlogArticle art where ub.timestamp > :datum1 and ub.timestamp < :datum2",
                        UserReport.class);
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
    public List<Object[]> getProblemArticles(int offen) {

        Query query = em
                .createNativeQuery("select b.Identifier, b.PartnerID, b.Config, b.AppdomainID, b.Warengruppenpfad, b.Saison, user.VORNAME, user.NACHNAME, b.Bemerkung1, b.Bemerkung2, b.Bemerkung3, b.BemerkungKAM, \n"
                        + "b.OFFEN, buchungen.Status\n"
                        + "from backlog b\n"
                        + "inner join (select buchungen.Identifier, max(buchungen.ID) as ID from buchungen group by buchungen.Identifier) c\n"
                        + "on b.Identifier = c.Identifier\n"
                        + "inner join buchungen on c.ID = buchungen.ID\n"
                        + "inner join user on user.ID = buchungen.User\n"
                        + "where \n"
                        + "(b.Bemerkung1 not like '' or b.Bemerkung2 not like '' or b.Bemerkung3 not like '') and b.OFFEN = " + offen);
//        System.out.println("query = " + query);
        List<Object[]> resultList = query.getResultList();

        return resultList;
    }

    public List<Object[]> getEditedDataByPartner(List<Integer> partner) {

        String inclause = QueryHelper.getInClause(partner);
        String q = "Select backlog.Identifier, backlog.PartnerID, backlog.Config, backlog.AppdomainID, backlog.Warengruppenpfad, backlog.Saison, max(buchungen.Timestamp), backlog.Bemerkung1, backlog.Bemerkung2, backlog.Bemerkung3, backlog.BemerkungKAM \n"
                + "from backlog left join buchungen on backlog.Identifier = buchungen.Identifier where backlog.PartnerID in " + inclause + " and backlog.OFFEN = 0 group by backlog.Identifier";

        String query2 = "select b.Identifier, b.PartnerID, b.Config, b.AppdomainID, b.Warengruppenpfad, b.Saison, buchungen.Timestamp, b.Bemerkung1, b.Bemerkung2, b.Bemerkung3, b.BemerkungKAM, \n"
                + "b.OFFEN, buchungen.Status\n"
                + "from backlog b\n"
                + "inner join (select buchungen.Identifier, max(buchungen.ID) as ID from buchungen group by buchungen.Identifier) c\n"
                + "on b.Identifier = c.Identifier\n"
                + "inner join buchungen on c.ID = buchungen.ID\n"
                + "inner join user on user.ID = buchungen.User\n"
                + "where b.PartnerID in " + inclause + " and b.OFFEN = 0 order by b.PartnerID";
        Query query = em.createNativeQuery(query2);

        List<Object[]> resultList = query.getResultList();

        return resultList;
    }

    public List<RejectReportOverview> getRejectReportOverview(Date date1, Date date2) {
        TypedQuery query = em.createQuery("select new de.contentcreation.pplive.reportingClasses.RejectReportOverview(b.partnerId, count(b.config)) from UpdateBuchung u inner join u.backlogArticle b where u.timestamp between :date1 and :date2 and (u.bemerkung1 is not null and u.bemerkung1 != '' or u.bemerkung2 is not null and u.bemerkung2 != '') group by b.partnerId", RejectReportOverview.class);       
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        List<RejectReportOverview> result = query.getResultList();
        return result;
    }

    public List<RejectReportBemerkung1> getRejectReportBemerkung1(Date date1, Date date2) {
        TypedQuery query = em.createQuery("select new de.contentcreation.pplive.reportingClasses.RejectReportBemerkung1(b.partnerId, u.bemerkung1, count(b.bemerkung1)) from UpdateBuchung u inner join u.backlogArticle b where u.timestamp between :date1 and :date2 and (u.bemerkung1 is not null or u.bemerkung1 != '') group by b.partnerId", RejectReportBemerkung1.class);
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        List<RejectReportBemerkung1> result = query.getResultList();
        return result;
    }

    public List<RejectReportBemerkung2> getRejectReportBemerkung2(Date date1, Date date2) {
        TypedQuery query = em.createQuery("select new de.contentcreation.pplive.reportingClasses.RejectReportBemerkung2(b.partnerId, u.bemerkung2, count(b.bemerkung2)) from UpdateBuchung u inner join u.backlogArticle b where u.timestamp between :date1 and :date2 and (u.bemerkung2 is not null and u.bemerkung2 != '') group by b.partnerId", RejectReportBemerkung2.class);
        query.setParameter("date1", date1);
        query.setParameter("date2", date2);
        List<RejectReportBemerkung2> result = query.getResultList();
        return result;
    }
}
