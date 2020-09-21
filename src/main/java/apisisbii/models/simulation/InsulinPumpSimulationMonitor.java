package apisisbii.models.simulation;

import org.cpntools.accesscpn.engine.highlevel.instance.Instance;
import org.cpntools.accesscpn.model.PlaceNode;

import apisisbii.models.DAO.InsulinPumpDetailedDAO;

public class InsulinPumpSimulationMonitor extends SimulationMonitor{
	private float bolus;
	private float cbolus;
	private float cartridgeCapacity;
	private float doseOfRateAdm;
	private int timeOfRateAdm;
	
	private float basal;
	private float cartridgeVolume;
	private int amountOfBatteryCharged;
	private int amountOfBatteryDischarged;
	private int timeCurrent;
	private float totalSelectedDose;
	
	InsulinPumpDetailedDAO insulinPumpDetailedDAO;
	
	public InsulinPumpSimulationMonitor() {
		// TODO Auto-generated constructor stub
		insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
	}
	
	public void configureRateAdm(String basalOption) throws Exception {
		if(basalOption.equals("standard")) {
			this.doseOfRateAdm = insulinPumpDetailedDAO.getValueOfParameter("DADMPD");
			this.timeOfRateAdm = (int) insulinPumpDetailedDAO.getValueOfParameter("TADMPD: TIME");
		}
		else if(basalOption.equals("personalized")) {
			this.doseOfRateAdm = insulinPumpDetailedDAO.getValueOfParameter("DADMPS");
			this.timeOfRateAdm = (int) insulinPumpDetailedDAO.getValueOfParameter("TADMPS: TIME");
		}
	}

	public void configureBolus() throws Exception {
		this.bolus = insulinPumpDetailedDAO.getValueOfParameter("BOLUS"); 
	}
	
	public void configureCBolus() throws Exception {
		this.cbolus = insulinPumpDetailedDAO.getValueOfParameter("CBOLUS");
	}
	
	public void configureCartridgeCapacity() throws Exception {
		this.cartridgeCapacity = insulinPumpDetailedDAO.getValueOfParameter("CAPCART");
	}

	public void countBatteries(Simulator simulator) throws Exception{
		amountOfBatteryCharged = amountOfBatteryDischarged = 0;
		for(Instance<PlaceNode> pi: simulator.getAllPlaceInstances()) {
			if(pi.toString().contains("Battery") && pi.toString().contains("Value")) {
				if(simulator.getMarkingAndConvertToString(pi).contains("1`1") ||
				   simulator.getMarkingAndConvertToString(pi).contains("1`0")) {
					this.amountOfBatteryCharged++;
				}
				else if(simulator.getMarkingAndConvertToString(pi).contains("1`2")) {
					this.amountOfBatteryDischarged++;
				}
			}
		}
	}
	
	public void configureCartridgeVolume(String marking) throws Exception{
		if(!marking.equals("empty")) {
			int i0, i1;
			i0 = marking.indexOf("`");
			i1 = marking.length();
			this.cartridgeVolume = Float.parseFloat(marking.substring(i0+1, i1));
		}
	}
	
	public void configureTotalSelectedDose(Simulator simulator, String basalOption) throws Exception{
		String place = "empty";
		if(basalOption.equals("standard")) {
			place = "SI.A";
		}
		else if(basalOption.equals("personalized")) {
			place = "PI.A";
		}
		
		String marking = simulator.getPlaceMarking(place);
		if(!marking.equals("empty")) {
			int i0, i1;
			i0 = marking.indexOf("`");
			i1 = marking.length();
			this.totalSelectedDose = Float.parseFloat(marking.substring(i0+1, i1));
		}
	}
	
	public void configureBasal(String marking){
		if(!marking.equals("empty")) {
			String[] separetedMarking = marking.split("\\+++\\n");
			for (String string : separetedMarking) {
				if(string.contains("1,")) {
					marking = string;
				}
			}
			int i0 = marking.indexOf("1,");
			int i1 = marking.indexOf(")");
			this.basal = Float.parseFloat(marking.substring(i0+2, i1));
		}
	}

	public void setTimeCurrent(int timeCurrent) {
		this.timeCurrent = timeCurrent;
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

	public float getCartridgeCapacity() {
		return cartridgeCapacity;
	}

	public void setCartridgeCapacity(float cartridgeCapacity) {
		this.cartridgeCapacity = cartridgeCapacity;
	}

	public float getDoseOfRateAdm() {
		return doseOfRateAdm;
	}

	public void setDoseOfRateAdm(float doseOfRateAdm) {
		this.doseOfRateAdm = doseOfRateAdm;
	}

	public int getTimeOfRateAdm() {
		return timeOfRateAdm;
	}

	public void setTimeOfRateAdm(int timeOfRateAdm) {
		this.timeOfRateAdm = timeOfRateAdm;
	}

	public float getBasal() {
		return basal;
	}

	public void setBasal(float basal) {
		this.basal = basal;
	}

	public float getCartridgeVolume() {
		return cartridgeVolume;
	}

	public void setCartridgeVolume(float cartridgeVolume) {
		this.cartridgeVolume = cartridgeVolume;
	}

	public int getAmountOfBatteryCharged() {
		return amountOfBatteryCharged;
	}

	public void setAmountOfBatteryCharged(int amountOfBatteryCharged) {
		this.amountOfBatteryCharged = amountOfBatteryCharged;
	}

	public int getAmountOfBatteryDischarged() {
		return amountOfBatteryDischarged;
	}

	public void setAmountOfBatteryDischarged(int amountOfBatteryDischarged) {
		this.amountOfBatteryDischarged = amountOfBatteryDischarged;
	}

	public float getTotalSelectedDose() {
		return totalSelectedDose;
	}

	public void setTotalSelectedDose(float totalSelectedDose) {
		this.totalSelectedDose = totalSelectedDose;
	}

	public float getBolus() {
		return bolus;
	}

	public int getTimeCurrent() {
		return timeCurrent;
	}
}
