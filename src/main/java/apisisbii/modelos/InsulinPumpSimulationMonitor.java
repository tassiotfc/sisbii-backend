package apisisbii.modelos;

import java.util.Arrays;

import org.cpntools.accesscpn.engine.highlevel.instance.Instance;
import org.cpntools.accesscpn.model.PlaceNode;

import apisisbii.modelos.DAO.CPNDetalhadoDAO;

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
	private String note;
	
	public void configureConstantParameters(String basalOption) throws Exception {
		CPNDetalhadoDAO CPNDetDAO = new CPNDetalhadoDAO();
		setBolus(CPNDetDAO.getParametroCPNDet("BOLUS")); 
		this.cbolus = CPNDetDAO.getParametroCPNDet("CBOLUS");
		this.cartridgeCapacity = CPNDetDAO.getParametroCPNDet("CAPCART");
		if(basalOption.equals("standard")) {
			this.doseOfRateAdm = CPNDetDAO.getParametroCPNDet("DADMPD");
			this.timeOfRateAdm = (int) CPNDetDAO.getParametroCPNDet("TADMPD: TIME");
		}
		else if(basalOption.equals("personalized")) {
			this.doseOfRateAdm = CPNDetDAO.getParametroCPNDet("DADMPS");
			this.timeOfRateAdm = (int) CPNDetDAO.getParametroCPNDet("TADMPS: TIME");
		}
	}
	
	public void configureCartridgeLevel(Simulator simulator) throws Exception{
		if(simulator.allPlacesAreMarked(Arrays.asList(new String[] {"EC"}))) {
			setNote("EC");
		}
		else {
			setNote("LCL");
		}
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
	
	public void configureTotalSelectedDose(String marking) throws Exception{
		if(!marking.equals("empty")) {
			int i0, i1;
			i0 = marking.indexOf("`");
			i1 = marking.length();
			this.totalSelectedDose = Float.parseFloat(marking.substring(i0+1, i1));
		}
	}
	
	public void configureBasal(String marking) throws Exception{
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
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
