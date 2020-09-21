package apisisbii.models.abstractinsulinpump;

import java.util.List;

public class InsulinPumpAbstract {
	
	private int bolus;
	private int cbolus;
	private int cartridgeCapacity;
	private int upperDoseLimit;
	private int lowerDoseLimit;
	private int infusionLimit;
	
	private int basal;
	private int amountBasal;
	private List<Integer> listBasal;
	
	public InsulinPumpAbstract(int bolus, int cbolus, int cartridgeCapacity, int upperDoseLimit, int lowerDoseLimit,
			int infusionLimit, int basal, int amountBasal, List<Integer> listBasal) {
		super();
		this.bolus = bolus;
		this.cbolus = cbolus;
		this.cartridgeCapacity = cartridgeCapacity;
		this.upperDoseLimit = upperDoseLimit;
		this.lowerDoseLimit = lowerDoseLimit;
		this.infusionLimit = infusionLimit;
		this.basal = basal;
		this.amountBasal = amountBasal;
		this.listBasal = listBasal;
	}
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
	
	public int getCartridgeCapacity() {
		return cartridgeCapacity;
	}
	
	public void setCartridgeCapacity(int cartridgeCapacity) {
		this.cartridgeCapacity = cartridgeCapacity;
	}
	
	public int getUpperDoseLimit() {
		return upperDoseLimit;
	}
	
	public void setUpperDoseLimit(int upperDoseLimit) {
		this.upperDoseLimit = upperDoseLimit;
	}
	
	public int getLowerDoseLimit() {
		return lowerDoseLimit;
	}
	
	public void setLowerDoseLimit(int lowerDoseLimit) {
		this.lowerDoseLimit = lowerDoseLimit;
	}
	
	public int getInfusionLimit() {
		return infusionLimit;
	}
	
	public void setInfusionLimit(int infusionLimit) {
		this.infusionLimit = infusionLimit;
	}

	public int getBasal() {
		return basal;
	}

	public void setBasal(int basal) {
		this.basal = basal;
	}

	public int getAmountBasal() {
		return amountBasal;
	}

	public void setAmountBasal(int amountBasal) {
		this.amountBasal = amountBasal;
	}

	public List<Integer> getListBasal() {
		return listBasal;
	}

	public void setListBasal(List<Integer> listBasal) {
		this.listBasal = listBasal;
	}
}
