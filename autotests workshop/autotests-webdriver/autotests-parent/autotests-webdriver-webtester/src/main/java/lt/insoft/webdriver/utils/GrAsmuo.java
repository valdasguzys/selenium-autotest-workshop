package lt.insoft.webdriver.utils;

/**
 * Klasė skirta talpinti testiniams duomenims iš gyventojų registro. Talpina juridinius ir fizinius asmenis.
 *
 */

public class GrAsmuo {
	private String asmensKodas;
	private String vardas;
	private String pavarde;
	private String gimimoData;
	private String lytis;
	private String pavadinimas;
	
	public String getAsmensKodas() {
		return asmensKodas;
	}
	public void setAsmensKodas(String asmensKodas) {
		this.asmensKodas = asmensKodas;
	}
	public String getVardas() {
		return vardas;
	}
	public void setVardas(String vardas) {
		this.vardas = vardas;
	}
	public String getPavarde() {
		return pavarde;
	}
	public void setPavarde(String pavarde) {
		this.pavarde = pavarde;
	}
	public String getGimimoData() {
		return gimimoData;
	}
	public void setGimimoData(String gimimoData) {
		this.gimimoData = gimimoData.substring(0,10);
	}
	public String getLytis() {
		return lytis;
	}
	public void setLytis(String lytis) {
		this.lytis = lytis;
	}
	public String getPavadinimas() {
		return pavadinimas;
	}
	public void setPavadinimas(String pavadinimas) {
		this.pavadinimas = pavadinimas;
	}
}

