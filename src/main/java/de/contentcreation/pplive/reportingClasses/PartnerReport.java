package de.contentcreation.pplive.reportingClasses;

/**
 * Die Implementierung eines PartnerReport-Objekts
 * @author Gösta Ostendorf (goesta.o@gmail.com)
 */
public class PartnerReport {
	private String nachname;
	private String vorname;
	private String name;
	private int partnerId;
	private int appdomainId;
	private String config;
	private String cgPath;
	private String saison;
	private String bemerkung1;
	private String bemerkung2;
	private String bemerkung3;
	private String bemerkungKAM;
	private boolean offen;

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getName() {
		return

		Character.toUpperCase(this.vorname.charAt(0))
				+ this.vorname.substring(1) + " "
				+ Character.toUpperCase(this.nachname.charAt(0))
				+ this.nachname.substring(1);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getConfig() {
		return this.config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public int getAppdomainId() {
		return this.appdomainId;
	}

	public void setAppdomainId(int appdomainId) {
		this.appdomainId = appdomainId;
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
		return bemerkungKAM;
	}

	public void setBemerkungKAM(String bemerkungKAM) {
		this.bemerkungKAM = bemerkungKAM;
	}

	public boolean isOffen() {
		return this.offen;
	}

	public void setOffen(boolean offen) {
		this.offen = offen;
	}

	public PartnerReport(String nachname, String vorname, int partnerId,
			int appdomainId, String config, String cgPath, String saison,
			String bemerkung1, String bemerkung2, String bemerkung3,
			String bemerkungKAM, boolean offen) {
		super();
		this.nachname = nachname;
		this.vorname = vorname;
		this.name = (vorname + " " + nachname);
		this.partnerId = partnerId;
		this.appdomainId = appdomainId;
		this.config = config;
		this.cgPath = cgPath;
		this.saison = saison;
		this.bemerkung1 = bemerkung1;
		this.bemerkung2 = bemerkung2;
		this.bemerkung3 = bemerkung3;
		this.bemerkungKAM = bemerkungKAM;
		this.offen = offen;
	}

}
