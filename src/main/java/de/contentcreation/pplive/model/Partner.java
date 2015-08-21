/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gostendorf
 */
@Entity
@Table(name = "partner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p order by p.name"),
    @NamedQuery(name = "Partner.findAllInBacklog", query = "SELECT p FROM Partner p where p.id in (Select distinct(b.partnerId) from BacklogArticle b where b.partnerId > 0 and b.offen = 1)  order by p.name"),
    @NamedQuery(name = "Partner.findById", query = "SELECT p FROM Partner p WHERE p.id = :id"),
    @NamedQuery(name = "Partner.findByName", query = "SELECT p FROM Partner p WHERE p.name = :name")})
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;

    public Partner() {
    }

    public Partner(Integer id) {
        this.id = id;
    }

    public Partner(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.contentcreation.pplive.model.Partner[ id=" + id + " ]";
    }

}
