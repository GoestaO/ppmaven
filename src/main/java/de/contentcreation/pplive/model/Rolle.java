package de.contentcreation.pplive.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Die Implementierung der Rolle
 *
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
@Entity
@Table(name = "rollen")
public class Rolle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "Rollenname")
    private String rollenname;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollenname() {
        return this.rollenname;
    }

    public void setRollenname(String rollenname) {
        this.rollenname = rollenname;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rolle other = (Rolle) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.rollenname, other.rollenname)) {
            return false;
        }
        return true;
    }

}
