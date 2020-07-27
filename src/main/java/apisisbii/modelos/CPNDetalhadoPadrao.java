package apisisbii.modelos;

public class CPNDetalhadoPadrao extends CPNDetalhado{
	private float basal;
	private float doseTaxaAdm;
	private int tempoTaxaAdm;
	private int tempoTaxaAdmBasal;
	
	public float getBasal() {
		return basal;
	}
	public void setBasal(float basal) {
		this.basal = basal;
	}
	public float getDoseTaxaAdm() {
		return doseTaxaAdm;
	}
	public void setDoseTaxaAdm(float doseTaxaAdm) {
		this.doseTaxaAdm = doseTaxaAdm;
	}
	public int getTempoTaxaAdm() {
		return tempoTaxaAdm;
	}
	public void setTempoTaxaAdm(int tempoTaxaAdm) {
		this.tempoTaxaAdm = tempoTaxaAdm;
	}
	public int getTempoTaxaAdmBasal() {
		return tempoTaxaAdmBasal;
	}
	public void setTempoTaxaAdmBasal(int tempoTaxaAdmBasal) {
		this.tempoTaxaAdmBasal = tempoTaxaAdmBasal;
	}
}
