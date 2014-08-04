package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
