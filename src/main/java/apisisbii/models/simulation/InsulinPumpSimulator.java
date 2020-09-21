package apisisbii.models.simulation;

import java.util.Arrays;
import java.util.List;

import org.cpntools.accesscpn.engine.highlevel.instance.Binding;

public class InsulinPumpSimulator {
	
	private final String inputFileCPN = "/home/tassio/0/Pump-AccuV2.cpn";
	private final String reportDirectory = "/home/tassio/0";
	private final String report = "SimulationReportInsulinPump.txt";
	private InsulinPumpSimulationMonitor insulinPumpSM;
	
	private Simulator simulator;
	
	public InsulinPumpSimulator() throws Exception {
		simulator = new Simulator(inputFileCPN, reportDirectory);
	}
	
	public void createSimulationMonitor() throws Exception {
		insulinPumpSM = new InsulinPumpSimulationMonitor();
		
		executeSimulation(Arrays.asList(new String[]{"System.Hardware.Start_Button"}),
				Arrays.asList(), Arrays.asList());
	}
	
	public void executeSimulation(InsulinPumpSimulationRestrictor simulationRestrictor) 
			throws Exception
	{
		simulator.enableTransitions(simulationRestrictor.getStopTransitions(), 
									simulationRestrictor.getExclusionTransitions());
		simulator.fireTransitions(simulationRestrictor.getTransitionsToFire());
	}
	
	public void executeSimulation(List<String> stopTransitions, List<String> exclusionTransitions, List<String> transitionsToFire) 
			throws Exception
	{
		simulator.enableTransitions(stopTransitions, exclusionTransitions);
		simulator.fireTransitions(transitionsToFire);
	}

	public void applyPartialDosage(
			InsulinPumpSimulationRestrictor simulationRestrictor) throws Exception
	{
		if(existsChargedBattery()) {
			updateTransitions();
			if(!transitionIsEnabled("Apply_Partial_Dosage") && transitionIsEnabled("Battery_Situation")){
				verifyBatteries(simulationRestrictor.getFinalState());
			}
			if(existsChargedBattery()) {
				executeSimulation(simulationRestrictor);
			}
		}
	}	
	
	private void verifyBatteries(String finalState) throws Exception {
		simulator.fireTransitions(Arrays.asList(new String[] {"Battery_Situation"}));
		Binding b = simulator.getBinding("Current_Value", "b = 0");
		while(b != null) {
			simulator.executeBinding(b);
			simulator.fireTransitions(Arrays.asList(new String[] {"Verify_Battery1"}));
			updateTransitions();
			if(transitionIsEnabled("Verify_Battery2")) {
				simulator.fireTransitions(Arrays.asList(new String[] {"Verify_Battery2"}));
				b = simulator.getBinding("Current_Value", "b = 0");
			}
			else {
				b = null;
			}
		}
		if(existsChargedBattery()) {
			if(finalState.equals("dischargedbatteries")){
					b = simulator.getBinding("Current_Value", "OkB = 0, b = 1");
					simulator.executeBinding(b);
			}
			else {
				b = simulator.getBinding("Current_Value", "OkB = 1, b = 1");
				simulator.executeBinding(b);
			}
		}
	}
	
	public boolean transitionIsEnabled(String transition)
	{
		for(String transitionAux: insulinPumpSM.getEnabledTransitions()) {
			if(transitionAux.contains(transition)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean existsChargedBattery() throws Exception
	{
		insulinPumpSM.countBatteries(simulator);
		if(insulinPumpSM.getAmountOfBatteryCharged() >= 1) {
			return true;
		}
		return false;
	}
	
	public InsulinPumpSimulationMonitor getSimulationMonitor() throws Exception 
	{
		return insulinPumpSM;
	}

	public InsulinPumpSimulationMonitor updateTransitions() 
	{
		insulinPumpSM.setEnabledTransitions(simulator.getSimulationMonitor().getEnabledTransitions());
		insulinPumpSM.setLastTransitionFired(simulator.getSimulationMonitor().getLastTransitionFired());
		return insulinPumpSM;
	}
	
	public void updateParameterBasal() throws Exception 
	{
		insulinPumpSM.configureBasal(simulator.getPlaceMarking("LA"));
	}
	
	public void updateParameterBolus() throws Exception 
	{
		insulinPumpSM.configureBolus();
	}
	
	public void updateParameterCBolus() throws Exception 
	{
		insulinPumpSM.configureCBolus();
	}
	
	public void updateParameterTotalSelectedDose(
			InsulinPumpSimulationRestrictor simulationRestrictor) throws Exception 
	{
		insulinPumpSM.configureTotalSelectedDose(simulator, simulationRestrictor.getBasalOption());
	}
	
	public void updateParameterTimeCurrent() throws Exception 
	{
		insulinPumpSM.setTimeCurrent(simulator.getTimeAndConvertToInt());
	}
	
	public void updateParameterBatteries() throws Exception 
	{
		insulinPumpSM.countBatteries(simulator);
	}
	
	public void updateCartridgeVolume() throws Exception 
	{
		insulinPumpSM.configureCartridgeVolume(simulator.getPlaceMarking("Cartridge.Cap_Cart"));
	}
			
	public void updateParameterCartridgeCapacity() throws Exception 
	{
		insulinPumpSM.configureCartridgeCapacity();
	}
	
	public void updateParameterRateAdm(String basalOption) throws Exception 
	{
		insulinPumpSM.configureRateAdm(basalOption);
	}
	
	public String generateSimulationReportInsulinPump() throws Exception
	{
		simulator.generateSimulationReport(report);
		return reportDirectory+"/"+report;
	}  
	
	public boolean destroySimulator()
	{
		return simulator.destroySimulator();
	}
}