package apisisbii.models.simulation;

public class InsulinPumpSimulationRestrictor extends SimulationRestrictor{ 
	private String basalOption;
	private String finalState;
	
	public String getBasalOption() {
		return basalOption;
	}
	public void setBasalOption(String basalOption) {
		this.basalOption = basalOption;
	}
	public String getFinalState() {
		return finalState;
	}
	public void setFinalState(String finalState) {
		this.finalState = finalState;
	}
}
