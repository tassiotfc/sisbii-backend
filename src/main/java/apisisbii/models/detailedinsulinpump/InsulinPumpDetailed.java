package apisisbii.models.detailedinsulinpump;

import java.util.List;

public class InsulinPumpDetailed {
	
	private float basal;
	private float bolus;
	private float cbolus;
	private float cartridgeCapacity;
	private float upperDoseLimit;
	private float lowerDoseLimit;
	private float infusionLimit;
	private float doseOfRateAdmSD;
	private int timeOfRateAdmSD;
	private int timeOfRateAdmBasalSD;
	private float doseOfRateAdmPZ;
	private int timeOfRateAdmPZ;
	private int timeOfRateAdmBasalPZ;
	
	private int amountBasal;
	private List<Float> listBasal;
	public InsulinPumpDetailed(float basal, float bolus, float cbolus, float cartridgeCapacity, float upperDoseLimit,
			float lowerDoseLimit, float infusionLimit, float doseOfRateAdmSD, int timeOfRateAdmSD,
			int timeOfRateAdmBasalSD, float doseOfRateAdmPZ, int timeOfRateAdmPZ, int timeOfRateAdmBasalPZ,
			int amountBasal, List<Float> listBasal) {
		super();
		this.basal = basal;
		this.bolus = bolus;
		this.cbolus = cbolus;
		this.cartridgeCapacity = cartridgeCapacity;
		this.upperDoseLimit = upperDoseLimit;
		this.lowerDoseLimit = lowerDoseLimit;
		this.infusionLimit = infusionLimit;
		this.doseOfRateAdmSD = doseOfRateAdmSD;
		this.timeOfRateAdmSD = timeOfRateAdmSD;
		this.timeOfRateAdmBasalSD = timeOfRateAdmBasalSD;
		this.doseOfRateAdmPZ = doseOfRateAdmPZ;
		this.timeOfRateAdmPZ = timeOfRateAdmPZ;
		this.timeOfRateAdmBasalPZ = timeOfRateAdmBasalPZ;
		this.amountBasal = amountBasal;
		this.listBasal = listBasal;
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
	public float getCartridgeCapacity() {
		return cartridgeCapacity;
	}
	public void setCartridgeCapacity(float cartridgeCapacity) {
		this.cartridgeCapacity = cartridgeCapacity;
	}
	public float getUpperDoseLimit() {
		return upperDoseLimit;
	}
	public void setUpperDoseLimit(float upperDoseLimit) {
		this.upperDoseLimit = upperDoseLimit;
	}
	public float getLowerDoseLimit() {
		return lowerDoseLimit;
	}
	public void setLowerDoseLimit(float lowerDoseLimit) {
		this.lowerDoseLimit = lowerDoseLimit;
	}
	public float getInfusionLimit() {
		return infusionLimit;
	}
	public void setInfusionLimit(float infusionLimit) {
		this.infusionLimit = infusionLimit;
	}
	public float getDoseOfRateAdmSD() {
		return doseOfRateAdmSD;
	}
	public void setDoseOfRateAdmSD(float doseOfRateAdmSD) {
		this.doseOfRateAdmSD = doseOfRateAdmSD;
	}
	public int getTimeOfRateAdmSD() {
		return timeOfRateAdmSD;
	}
	public void setTimeOfRateAdmSD(int timeOfRateAdmSD) {
		this.timeOfRateAdmSD = timeOfRateAdmSD;
	}
	public int getTimeOfRateAdmBasalSD() {
		return timeOfRateAdmBasalSD;
	}
	public void setTimeOfRateAdmBasalSD(int timeOfRateAdmBasalSD) {
		this.timeOfRateAdmBasalSD = timeOfRateAdmBasalSD;
	}
	public float getDoseOfRateAdmPZ() {
		return doseOfRateAdmPZ;
	}
	public void setDoseOfRateAdmPZ(float doseOfRateAdmPZ) {
		this.doseOfRateAdmPZ = doseOfRateAdmPZ;
	}
	public int getTimeOfRateAdmPZ() {
		return timeOfRateAdmPZ;
	}
	public void setTimeOfRateAdmPZ(int timeOfRateAdmPZ) {
		this.timeOfRateAdmPZ = timeOfRateAdmPZ;
	}
	public int getTimeOfRateAdmBasalPZ() {
		return timeOfRateAdmBasalPZ;
	}
	public void setTimeOfRateAdmBasalPZ(int timeOfRateAdmBasalPZ) {
		this.timeOfRateAdmBasalPZ = timeOfRateAdmBasalPZ;
	}
	public int getAmountBasal() {
		return amountBasal;
	}
	public void setAmountBasal(int amountBasal) {
		this.amountBasal = amountBasal;
	}
	public List<Float> getListBasal() {
		return listBasal;
	}
	public void setListBasal(List<Float> listBasal) {
		this.listBasal = listBasal;
	}
}
