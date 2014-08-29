package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Die Implementierung der Bemerkung
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */

@Entity
@Table(name = "bemerkungen")
public class Bemerkung implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	@Column(name = "Bemerkung")
	private String bemerkung;

	public Long getID() {
		return this.id;
	}

	public void setID(Long id) {
		this.id = id;
	}

	public String getBemerkung() {
		return this.bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
}
