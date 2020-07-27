package apisisbii.modelos;

public class CPNDetalhado {
	private float bolus;
	private float cbolus;
	private float limiteDoseInferior;
	private float limiteDoseSuperior;
	private float limiteInfusao;
	private float capCartucho;
	private int qtdBasal;
	
	public float getBolus() {
		return bolus;
	}
	public void setBolus(float bolus) {
		this.bolus = bolus;
	}
	public float getCbolus() {
		return cbolus;
	}
	public void setCbolus(float cbolus) {
		this.cbolus = cbolus;
	}
	public float getLimiteDoseInferior() {
		return limiteDoseInferior;
	}
	public void setLimiteDoseInferior(float limiteDoseInferior) {
		this.limiteDoseInferior = limiteDoseInferior;
	}
	public float getLimiteDoseSuperior() {
		return limiteDoseSuperior;
	}
	public void setLimiteDoseSuperior(float limiteDoseSuperior) {
		this.limiteDoseSuperior = limiteDoseSuperior;
	}
	public float getLimiteInfusao() {
		return limiteInfusao;
	}
	public void setLimiteInfusao(float limiteInfusao) {
		this.limiteInfusao = limiteInfusao;
	}
	public float getCapCartucho() {
		return capCartucho;
	}
	public void setCapCartucho(float capCartucho) {
		this.capCartucho = capCartucho;
	}
	public int getQtdBasal() {
		return qtdBasal;
	}
	public void setQtdBasal(int qtdBasal) {
		this.qtdBasal = qtdBasal;
	}
}
