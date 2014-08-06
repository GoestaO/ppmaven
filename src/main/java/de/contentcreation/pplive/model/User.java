package de.contentcreation.pplive.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Lob
	private String nachname;
	@Lob
	private String nick;
	@Lob
	private String passwort;
	@Lob
	private String vorname;
	@JoinColumn(name = "Rolle")
	private Rolle rolle;
	@Transient
	private boolean isValid;

	public Rolle getRolle() {
		return this.rolle;
	}

	public void setRolle(Rolle rolle) {
		this.rolle = rolle;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
