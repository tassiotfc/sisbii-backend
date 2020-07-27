package apisisbii.modelos;

import java.util.List;

public class CPNDetalhadoPersonalizado extends CPNDetalhado{
	private List<Float> basal;
	private float doseTaxaAdm;
	private int tempoTaxaAdm;
	private int tempoTaxaAdmBasal;
	
	public List<Float> getBasal() {
		return basal;
	}
	public void setBasal(List<Float> basal) {
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