package apisisbii.controladores;

import java.io.File;
import java.util.Arrays;

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
@RequestMapping(value="api")
public class InsulinPumpSimulatorControl {
	InsulinPumpSimulator insulinPumpSimulator;
	
	@GetMapping("/preparesimulationenvironment")
	public InsulinPumpSimulationMonitor prepareSimulationEnvironment(){
		try {
			insulinPumpSimulator = new InsulinPumpSimulator();
			return insulinPumpSimulator.createAndConfigureSimulationMonitor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/setuppump")
	public InsulinPumpSimulationMonitor setupInsulinPump(
			@RequestBody String basalOption) throws Exception {
		return insulinPumpSimulator.setupInsulinPump(basalOption);
	}
	
	@PostMapping("/simulatemodel")
	public InsulinPumpSimulationMonitor simulateModel(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor)
	{
		try {
			return insulinPumpSimulator.simulateModel(simulationRestrictor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/simulateinfusion")
	public InsulinPumpSimulationMonitor simulateInfusion(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor)
	{
		try {
			insulinPumpSimulator.getSimulator().fireTransitions(
					Arrays.asList(new String[] {"Finish_Partial_App"}));
			return insulinPumpSimulator.applyPartialDosage(simulationRestrictor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/preparesimulateinfusion")
	public InsulinPumpSimulationMonitor prepareSimulationInfusion(
			@RequestBody InsulinPumpSimulationRestrictor simulationRestrictor)
	{
		try {
			insulinPumpSimulator.getSimulator().fireTransitions(simulationRestrictor.getTransitionsToFire());
			simulationRestrictor.setTransitionsToFire(Arrays.asList());
			insulinPumpSimulator.updateTransitions();
			if(insulinPumpSimulator.transitionIsEnabled("Prepare_Partial_Application")) {
				String place = "empty";
				if(simulationRestrictor.getBasalOption().equals("standard")) {
					place = "SI.A";
				}
				else if(simulationRestrictor.getBasalOption().equals("personalized")) {
					place = "PI.A";
				}
				insulinPumpSimulator.getInsulinPumpSimulationMonitor().configureTotalSelectedDose(
						insulinPumpSimulator.getSimulator().getPlaceMarking(place));
				insulinPumpSimulator.getSimulator().fireTransitions(
						Arrays.asList(new String[] {"Prepare_Partial_Application"}));
				insulinPumpSimulator.applyPartialDosage(simulationRestrictor);
			}
			else if(insulinPumpSimulator.transitionIsEnabled("Update1")) {
				insulinPumpSimulator.getSimulator().fireTransitions(Arrays.asList(new String[] {"Update1"}));
				insulinPumpSimulator.getInsulinPumpSimulationMonitor().configureCartridgeLevel(
						insulinPumpSimulator.getSimulator());
			}
			return insulinPumpSimulator.updateTransitions();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
		
	@GetMapping("/finishsimulation")
	public boolean finishSimulation() throws Exception {
		return insulinPumpSimulator.getSimulator().destroySimulator();
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