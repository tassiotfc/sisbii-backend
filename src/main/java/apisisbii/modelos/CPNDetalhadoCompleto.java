package apisisbii.modelos;

import java.util.List;

public class CPNDetalhadoCompleto extends CPNDetalhado{
	private float basal;
	private List<Float> listaBasal;
	private float doseTaxaAdmPD;
	private int tempoTaxaAdmPD;
	private int tempoTaxaAdmBasalPD;
	private float doseTaxaAdmPS;
	private int tempoTaxaAdmPS;
	private int tempoTaxaAdmBasalPS;
	public float getBasal() {
		return basal;
	}
	public void setBasal(float basal) {
		this.basal = basal;
	}
	public List<Float> getListaBasal() {
		return listaBasal;
	}
	public void setListaBasal(List<Float> listaBasal) {
		this.listaBasal = listaBasal;
	}
	public float getDoseTaxaAdmPD() {
		return doseTaxaAdmPD;
	}
	public void setDoseTaxaAdmPD(float doseTaxaAdmPD) {
		this.doseTaxaAdmPD = doseTaxaAdmPD;
	}
	public int getTempoTaxaAdmPD() {
		return tempoTaxaAdmPD;
	}
	public void setTempoTaxaAdmPD(int tempoTaxaAdmPD) {
		this.tempoTaxaAdmPD = tempoTaxaAdmPD;
	}
	public int getTempoTaxaAdmBasalPD() {
		return tempoTaxaAdmBasalPD;
	}
	public void setTempoTaxaAdmBasalPD(int tempoTaxaAdmBasalPD) {
		this.tempoTaxaAdmBasalPD = tempoTaxaAdmBasalPD;
	}
	public float getDoseTaxaAdmPS() {
		return doseTaxaAdmPS;
	}
	public void setDoseTaxaAdmPS(float doseTaxaAdmPS) {
		this.doseTaxaAdmPS = doseTaxaAdmPS;
	}
	public int getTempoTaxaAdmPS() {
		return tempoTaxaAdmPS;
	}
	public void setTempoTaxaAdmPS(int tempoTaxaAdmPS) {
		this.tempoTaxaAdmPS = tempoTaxaAdmPS;
	}
	public int getTempoTaxaAdmBasalPS() {
		return tempoTaxaAdmBasalPS;
	}
	public void setTempoTaxaAdmBasalPS(int tempoTaxaAdmBasalPS) {
		this.tempoTaxaAdmBasalPS = tempoTaxaAdmBasalPS;
	}
}
