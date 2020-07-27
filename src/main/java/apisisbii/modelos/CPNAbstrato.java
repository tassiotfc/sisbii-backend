package apisisbii.modelos;

public class CPNAbstrato {
	private int bolus;
	private int cbolus;
	private int limiteDoseInferior;
	private int limiteDoseSuperior;
	private int limiteInfusao;
	private int capCartucho;
	private int qtdBasal;
	
	public int getBolus() {
		return bolus;
	}
	public void setBolus(int bolus) {
		this.bolus = bolus;
	}
	public int getCbolus() {
		return cbolus;
	}
	public void setCbolus(int cbolus) {
		this.cbolus = cbolus;
	}
	public int getLimiteDoseInferior() {
		return limiteDoseInferior;
	}
	public void setLimiteDoseInferior(int limiteDoseInferior) {
		this.limiteDoseInferior = limiteDoseInferior;
	}
	public int getLimiteDoseSuperior() {
		return limiteDoseSuperior;
	}
	public void setLimiteDoseSuperior(int limiteDoseSuperior) {
		this.limiteDoseSuperior = limiteDoseSuperior;
	}
	public int getLimiteInfusao() {
		return limiteInfusao;
	}
	public void setLimiteInfusao(int limiteInfusao) {
		this.limiteInfusao = limiteInfusao;
	}
	public int getCapCartucho() {
		return capCartucho;
	}
	public void setCapCartucho(int capCartucho) {
		this.capCartucho = capCartucho;
	}
	public int getQtdBasal() {
		return qtdBasal;
	}
	public void setQtdBasal(int qtdBasal) {
		this.qtdBasal = qtdBasal;
	}
}
