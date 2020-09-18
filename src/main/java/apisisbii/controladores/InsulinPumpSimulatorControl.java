package apisisbii.controladores;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.modelos.InsulinPumpSimulator;
import apisisbii.modelos.InsulinPumpSimulationMonitor;
import apisisbii.modelos.InsulinPumpSimulationRestrictor;

import java.io.FileInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value="simulation")
public class InsulinPumpSimulatorControl {
	InsulinPumpSimulator insulinPumpSimulator;
	
	@GetMapping("/prepareenvironment")
	public InsulinPumpSimulationMonitor prepareSimulationEnvironment() 
			throws Exception 
	{
		insulinPumpSimulator = new InsulinPumpSimulator();
		insulinPumpSimulator.createSimulationMonitor();
		insulinPumpSimulator.updateParameterCartridgeCapacity();
		return insulinPumpSimulator.updateTransitions();
	}
	
	@PostMapping("/setuppump")
	public InsulinPumpSimulationMonitor setupInsulinPump (
			@RequestBody String basalOption) throws Exception 
	{
		List<String> fireTransitionsAux = Arrays.asList();
		if(basalOption.equals("standard")) {
			fireTransitionsAux = Arrays.asList(new String[]{"SMC.Standard_Conf"});			
		}
		else if(basalOption.equals("personalized")) {
			fireTransitionsAux = Arrays.asList(new String[]{"PMC.Standard_Conf"});
		}
		insulinPumpSimulator.executeSimulation(Arrays.asList(new String[]{"Standard_Conf"}),
				Arrays.asList(new String[]{"Battery_Situation"}), 
				fireTransitionsAux);
		insulinPumpSimulator.executeSimulation(Arrays.asList(new String[]{"Adm_Basal", "Adm_Bolus", "Adm_CBolus"}), 
				Arrays.asList(new String[]{"Battery_Situation"}), 
				Arrays.asList());
		insulinPumpSimulator.updateParameterBasal();
		insulinPumpSimulator.updateParameterBolus();
		insulinPumpSimulator.updateParameterBolus();
		insulinPumpSimulator.updateParameterRateAdm(basalOption);
		insulinPumpSimulator.updateCartridgeVolume();
		insulinPumpSimulator.updateParameterBatteries();
		insulinPumpSimulator.updateParameterTimeCurrent();
		return insulinPumpSimulator.updateTransitions();
	}
	
	@PostMapping("/simulate")
	public InsulinPumpSimulationMonitor simulate(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor) 
					throws Exception
	{
		insulinPumpSimulator.executeSimulation(simulationRestrictor);
		return insulinPumpSimulator.updateTransitions();
	}
	
	@PostMapping("/selection")
	public InsulinPumpSimulationMonitor goToSelection(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor) 
					throws Exception
	{
		insulinPumpSimulator.executeSimulation(simulationRestrictor);
		insulinPumpSimulator.updateParameterBasal();
		insulinPumpSimulator.updateCartridgeVolume();
		insulinPumpSimulator.updateParameterBatteries();
		insulinPumpSimulator.updateParameterTimeCurrent();
		return insulinPumpSimulator.updateTransitions();
	}
	
	@PostMapping("/simulateinfusion")
	public InsulinPumpSimulationMonitor simulateInfusion(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor) throws Exception
	{
		insulinPumpSimulator.executeSimulation(Arrays.asList(), Arrays.asList(), 
				Arrays.asList(new String[] {"Finish_Partial_App"}));
		insulinPumpSimulator.applyPartialDosage(simulationRestrictor);
		
		insulinPumpSimulator.updateCartridgeVolume();
		insulinPumpSimulator.updateParameterBatteries();
		insulinPumpSimulator.updateParameterTimeCurrent();
		return insulinPumpSimulator.updateTransitions();
	}
	
	@PostMapping("/preparesimulateinfusion")
	public InsulinPumpSimulationMonitor prepareSimulationInfusion(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor) throws Exception
	{
		insulinPumpSimulator.executeSimulation(Arrays.asList(), Arrays.asList(), 
				simulationRestrictor.getTransitionsToFire());
		simulationRestrictor.setTransitionsToFire(Arrays.asList());
		
		insulinPumpSimulator.updateTransitions();
		if(insulinPumpSimulator.transitionIsEnabled("Prepare_Partial_Application")) {
			insulinPumpSimulator.updateParameterTotalSelectedDose(simulationRestrictor);
			insulinPumpSimulator.executeSimulation(Arrays.asList(), Arrays.asList(), 
					Arrays.asList(new String[] {"Prepare_Partial_Application"}));
			insulinPumpSimulator.applyPartialDosage(simulationRestrictor);
		}
		else if(insulinPumpSimulator.transitionIsEnabled("Update1")) {
			insulinPumpSimulator.executeSimulation(Arrays.asList(), Arrays.asList(), 
					Arrays.asList(new String[] {"Update1"}));
		}
		insulinPumpSimulator.updateCartridgeVolume();
		insulinPumpSimulator.updateParameterBatteries();
		insulinPumpSimulator.updateParameterTimeCurrent();
		return insulinPumpSimulator.updateTransitions();
	}
		
	@GetMapping("/finish")
	public boolean finishSimulation() throws Exception 
	{
		return insulinPumpSimulator.destroySimulator();
	}
		
	@GetMapping("/downloadreport")
	public ResponseEntity<Object> downloadSimulationReport() throws Exception 
	{
		String filename = insulinPumpSimulator.generateSimulationReportInsulinPump();
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
				.contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		return responseEntity;
	}
}