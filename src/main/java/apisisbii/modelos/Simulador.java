package apisisbii.modelos;

import java.util.List;

public class Simulador {
	private float basal;
	private float bolus;
	private float cbolus;
	private float volCartucho;
	private float capCartucho;
	private float doseTaxaAdm;
	private int tempoTaxaAdm;
	private int qtdBatCarregada;
	private int qtdBatDescarregada;
	private int tempoAtual;
	private List<String> transicoesHabilitadas;
	private String transicaoDisparada;
	private OpcoesSimulacao opcoesSimulacao;
	private float doseTotalSelecionada;
	public float getDoseTotalSelecionada() {
		return doseTotalSelecionada;
	}
	public void setDoseTotalSelecionada(float doseTotalSelecionada) {
		this.doseTotalSelecionada = doseTotalSelecionada;
	}
	public OpcoesSimulacao getOpcoesSimulacao() {
		return opcoesSimulacao;
	}
	public void setOpcoesSimulacao(OpcoesSimulacao opcoesSimulacao) {
		this.opcoesSimulacao = opcoesSimulacao;
	}
	public float getBasal() {
		return basal;
	}
	public void setBasal(float basal) {
		this.basal = basal;
	}
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
	public float getVolCartucho() {
		return volCartucho;
	}
	public void setVolCartucho(float volCartucho) {
		this.volCartucho = volCartucho;
	}
	public float getCapCartucho() {
		return capCartucho;
	}
	public void setCapCartucho(float capCartucho) {
		this.capCartucho = capCartucho;
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
	public int getQtdBatCarregada() {
		return qtdBatCarregada;
	}
	public void setQtdBatCarregada(int qtdBatCarregada) {
		this.qtdBatCarregada = qtdBatCarregada;
	}
	public int getQtdBatDescarregada() {
		return qtdBatDescarregada;
	}
	public void setQtdBatDescarregada(int qtdBatDescarregada) {
		this.qtdBatDescarregada = qtdBatDescarregada;
	}
	public int getTempoAtual() {
		return tempoAtual;
	}
	public void setTempoAtual(int tempoAtual) {
		this.tempoAtual = tempoAtual;
	}
	public List<String> getTransicoesHabilitadas() {
		return transicoesHabilitadas;
	}
	public void setTransicoesHabilitadas(List<String> transicoesHabilitadas) {
		this.transicoesHabilitadas = transicoesHabilitadas;
	}
	public String getTransicaoDisparada() {
		return transicaoDisparada;
	}
	public void setTransicaoDisparada(String transicaoDisparada) {
		this.transicaoDisparada = transicaoDisparada;
	}
}
