package apisisbii.modelos;

import java.util.List;

public class CPNAbstratoCompleto extends CPNAbstrato{
	private int basal;
	private List<Integer> listaBasal;
	
	public int getBasal() {
		return basal;
	}
	public void setBasal(int basal) {
		this.basal = basal;
	}
	public List<Integer> getListaBasal() {
		return listaBasal;
	}
	public void setListaBasal(List<Integer> listaBasal) {
		this.listaBasal = listaBasal;
	}
}