package de.contentcreation.pplive.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Die Implementierung des Backlogartikels
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Entity
@Table(name = "backlog")
public class BacklogArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "Identifier")
    private String identifier;
    @Column(name = "EAN")
    private long ean;
    @Column(name = "AppdomainID")
    private int appdomainId;
    @Column(name = "Config")
    private String config;
    @Column(name = "Eingepflegt")
    private boolean eingepflegt;
    @Column(name = "PartnerID")
    private int partnerId;
    @Column(name = "Warengruppenpfad")
    private String cgPath;
    @Column(name = "Saison")
    private String saison;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Datum")
    private Date datum;
    @Column(name = "OFFEN")
    private boolean offen;

    @Column(name = "Bemerkung1")
    private String bemerkung1 = null;

    @Column(name = "Bemerkung2")
    private String bemerkung2 = null;

    @Column(name = "Bemerkung3")
    private String bemerkung3 = null;

    @Column(name = "BemerkungKAM")
    private String bemerkungKAM = null;
    @Column(name = "Counter")
    private int counter;

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getEan() {
        return ean;
    }

    public void setEan(long ean) {
        this.ean = ean;
    }

    public int getAppdomainId() {
        return this.appdomainId;
    }

    public void setAppdomainId(int appdomainId) {
        this.appdomainId = appdomainId;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public boolean isEingepflegt() {
        return this.eingepflegt;
    }

    public void setEingepflegt(boolean eingepflegt) {
        this.eingepflegt = eingepflegt;
    }

    public int getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getCgPath() {
        return this.cgPath;
    }

    public void setCgPath(String cgPath) {
        this.cgPath = cgPath;
    }

    public String getSaison() {
        return this.saison;
    }

    public void setSaison(String saison) {
        this.saison = saison;
    }

    public Date getDatum() {
        return this.datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public boolean isOffen() {
        return this.offen;
    }

    public void setOffen(boolean offen) {
        this.offen = offen;
    }

    public static long getSerialversionuid() {
        return 1L;
    }

    public String getBemerkung1() {
        return this.bemerkung1;
    }

    public void setBemerkung1(String bemerkung1) {
        this.bemerkung1 = bemerkung1;
    }

    public String getBemerkung2() {
        return this.bemerkung2;
    }

    public void setBemerkung2(String bemerkung2) {
        this.bemerkung2 = bemerkung2;
    }

    public String getBemerkung3() {
        return this.bemerkung3;
    }

    public void setBemerkung3(String bemerkung3) {
        this.bemerkung3 = bemerkung3;
    }

    public String getBemerkungKAM() {
        return this.bemerkungKAM;
    }

    public void setBemerkungKAM(String bemerkungKAM) {
        this.bemerkungKAM = bemerkungKAM;
    }

    @Override
    public String toString() {
        return identifier;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.identifier);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BacklogArticle other = (BacklogArticle) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.bemerkung1, other.bemerkung1)) {
            return false;
        }
        if (!Objects.equals(this.bemerkung2, other.bemerkung2)) {
            return false;
        }
//        if (!Objects.equals(this.bemerkung3, other.bemerkung3)) {
//            return false;
//        }
        
//        if (!(this.bemerkung1 == other.bemerkung1)) {
//            return false;
//        }
//
//        if (!(this.bemerkung2 == other.bemerkung2)) {
//            return false;
//        }
//
//        if (!(this.bemerkung3 == other.bemerkung3)) {
//            return false;
//        }
        return true;
    }

    public final boolean equalsWithNulls(Object obj) {
        final BacklogArticle other = (BacklogArticle) obj;

        if (!(this.bemerkung1 == null && other.bemerkung1 == null)) {
            return false;
        }

        if (!(this.bemerkung2 == null && other.bemerkung2 == null)) {
            return false;
        }
        return true;
    }

}
