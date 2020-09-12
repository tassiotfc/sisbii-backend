package apisisbii.modelos;

import java.util.Arrays;
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
	
	public InsulinPumpSimulationMonitor createAndConfigureSimulationMonitor() throws Exception {
		insulinPumpSM = new InsulinPumpSimulationMonitor();
		
		simulator.enableTransitions(Arrays.asList(new String[]{"System.Hardware.Start_Button"}), Arrays.asList());
		insulinPumpSM.setEnabledTransitions(simulator.getSimulationMonitor().getEnabledTransitions());
		insulinPumpSM.setLastTransitionFired(simulator.getSimulationMonitor().getLastTransitionFired());
		insulinPumpSM.setTimeCurrent(simulator.getTimeAndConvertToInt());
		insulinPumpSM.countBatteries(simulator);		
		
		return insulinPumpSM;
	}
	
	public InsulinPumpSimulationMonitor setupInsulinPump(
			String basalOption) throws Exception{
		simulator.enableTransitions(Arrays.asList(new String[]{"Standard_Conf"}), 
				Arrays.asList(new String[]{"Battery_Situation"}));
		if(basalOption.equals("standard")) {
			simulator.fireTransitions(Arrays.asList(new String[]{"SMC.Standard_Conf"}));			
		}
		else if(basalOption.equals("personalized")) {
			simulator.fireTransitions(Arrays.asList(new String[]{"PMC.Standard_Conf"}));
		}
		simulator.enableTransitions(Arrays.asList(new String[]{"Adm_Basal", "Adm_Bolus", "Adm_CBolus"}), 
									Arrays.asList(new String[]{"Battery_Situation"}));
		
		insulinPumpSM.setEnabledTransitions(simulator.getSimulationMonitor().getEnabledTransitions());
		insulinPumpSM.setLastTransitionFired(simulator.getSimulationMonitor().getLastTransitionFired());
		insulinPumpSM.countBatteries(simulator);
		insulinPumpSM.setTimeCurrent(simulator.getTimeAndConvertToInt());
		insulinPumpSM.configureBasal(simulator.getPlaceMarking("LA"));
		insulinPumpSM.configureCartridgeVolume(simulator.getPlaceMarking("Cartridge.Cap_Cart"));
		insulinPumpSM.configureConstantParameters(basalOption);
		return insulinPumpSM;
	}
	
	public InsulinPumpSimulationMonitor simulateModel(InsulinPumpSimulationRestrictor simulationRestrictor) 
			throws Exception{
		System.out.println(simulationRestrictor.getStopTransitions() + " - " + simulationRestrictor.getExclusionTransitions());
		simulator.enableTransitions(simulationRestrictor.getStopTransitions(), 
									simulationRestrictor.getExclusionTransitions());
		simulator.fireTransitions(simulationRestrictor.getTransitionsToFire());
		updateTransitions();
		return insulinPumpSM;
	}

	public InsulinPumpSimulationMonitor applyPartialDosage(
			InsulinPumpSimulationRestrictor simulationRestrictor) throws Exception{
		if(existsChargedBattery()) {
			updateTransitions();
			if(!transitionIsEnabled("Apply_Partial_Dosage") && transitionIsEnabled("Battery_Situation")){
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
					if(simulationRestrictor.getFinalState().equals("dischargedbatteries")){
							b = simulator.getBinding("Current_Value", "OkB = 0, b = 1");
							simulator.executeBinding(b);
					}
					else {
						//Instance<? extends Transition> t = simulator.getTransition("Current__Value", "b = 1");
						//simulator.executeTransition(t);
						b = simulator.getBinding("Current_Value", "OkB = 1, b = 1");
						simulator.executeBinding(b);
					}
				}
			}
			if(existsChargedBattery()) {
				simulateModel(simulationRestrictor);
			}
		}
		insulinPumpSM.countBatteries(simulator);
		insulinPumpSM.setTimeCurrent(simulator.getTimeAndConvertToInt());
		insulinPumpSM.configureCartridgeVolume(simulator.getPlaceMarking("Cartridge.Cap_Cart"));
		updateTransitions();
		return insulinPumpSM;
	}		
	
	public boolean transitionIsEnabled(String transition) {
		for(String transitionAux: insulinPumpSM.getEnabledTransitions()) {
			if(transitionAux.contains(transition)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existsChargedBattery() throws Exception{
		insulinPumpSM.countBatteries(simulator);
		if(insulinPumpSM.getAmountOfBatteryCharged() >= 1) {
			return true;
		}
		return false;
	}
	
	public InsulinPumpSimulationMonitor updateTransitions() {
		insulinPumpSM.setEnabledTransitions(simulator.getSimulationMonitor().getEnabledTransitions());
		insulinPumpSM.setLastTransitionFired(simulator.getSimulationMonitor().getLastTransitionFired());
		return insulinPumpSM;
	}
		
	public String generateSimulationReportInsulinPump() throws Exception{
		simulator.generateSimulationReport(report);
		return reportDirectory+"/"+report;
	}  
	
	public Simulator getSimulator() {
		return simulator;
	}
	
	public InsulinPumpSimulationMonitor getInsulinPumpSimulationMonitor() {
		return insulinPumpSM;
	}
}