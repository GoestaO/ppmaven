package de.contentcreation.pplive.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Die Implementierung der Update-Buchung
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */

@Entity
@Table(name = "buchungen")
public class UpdateBuchung implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	@OneToOne
	@JoinColumn(name = "Identifier")
	private BacklogArticle backlogArticle;
	@Lob
	@Column(name = "Bemerkung1")
	private String bemerkung1;
	@Lob
	@Column(name = "Bemerkung2")
	private String bemerkung2;
	@Lob
	@Column(name = "Bemerkung3")
	private String bemerkung3;
	@Column(name = "BemerkungKAM")
	private String bemerkungKAM;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Timestamp")
	private Date timestamp;
	@OneToOne
	@JoinColumn(name = "User")
	private User user;
	@Column(name = "Status")
	private String status;
	@Column(name = "Saison")
	private String saison;

	public String getSaison() {
		return this.saison;
	}

	public void setSaison(String saison) {
		this.saison = saison;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BacklogArticle getBacklogArticle() {
		return this.backlogArticle;
	}

	public void setBacklogArticle(BacklogArticle backlogArticle) {
		this.backlogArticle = backlogArticle;
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

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBemerkungKAM() {
		return this.bemerkungKAM;
	}

	public void setBemerkungKAM(String bemerkungKAM) {
		this.bemerkungKAM = bemerkungKAM;
	}
}
