package apisisbii.modelos;

import java.util.List;

public class CPNAbstratoPersonalizado extends CPNAbstrato{
	private List<Integer> basal;
	
	public List<Integer> getBasal() {
		return basal;
	}
	public void setBasal(List<Integer> basal) {
		this.basal = basal;
	}
}