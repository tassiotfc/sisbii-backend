package apisisbii.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.models.DAO.InsulinPumpDetailedDAO;
import apisisbii.models.detailedinsulinpump.InsulinPumpDetailed;

@RestController
@RequestMapping(value="config")
public class InsulinPumpDetailedConfigurationControl {
	
	@GetMapping("/listparametersdetailed")
	public InsulinPumpDetailed listParametersInsulinPumpDetailed() throws Exception {
		InsulinPumpDetailedDAO insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
		
		ArrayList<String> parametersName = new ArrayList<String>(Arrays.asList(
				new String[]{"BASAL", "BOLUS", "CBOLUS", "CAPCART", "UPPERDOSELIMIT", 
						"LOWERDOSELIMIT", "INFUSIONLIMIT", 
						"DADMPD", "TADMPD: TIME", "TADMBPD: TIME",
						"DADMPS", "TADMPS: TIME", "TADMBPS: TIME", 
						"QTDBASAL"}));
		
		List<Float> parametersValue = insulinPumpDetailedDAO.getValueOfParametersInsulinPump(parametersName);
		List<Float> valuesBasal = insulinPumpDetailedDAO.readFileBasal();
		
		InsulinPumpDetailed insulinPumpDetailed = new InsulinPumpDetailed(
				parametersValue.get(0), parametersValue.get(1), parametersValue.get(2), 
				parametersValue.get(3), parametersValue.get(4),	parametersValue.get(5),	
				parametersValue.get(6), parametersValue.get(7),
				(int) Float.parseFloat(parametersValue.get(8).toString()), 
				(int )Float.parseFloat(parametersValue.get(9).toString()),
				parametersValue.get(10),
				(int) Float.parseFloat(parametersValue.get(11).toString()), 
				(int )Float.parseFloat(parametersValue.get(12).toString()),
				(int )Float.parseFloat(parametersValue.get(13).toString()),
				valuesBasal);
		return insulinPumpDetailed;
	}

	@PostMapping("/updateparametersdetailed")
	public boolean updateParametersDetailedInsulinPump(
			@RequestBody InsulinPumpDetailed insulinPumpDetailed) throws Exception {
		InsulinPumpDetailedDAO insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
		
		ArrayList<String> parametersName = new ArrayList<String>(Arrays.asList(
				new String[]{"BASAL", "BOLUS", "CBOLUS", "CAPCART", "UPPERDOSELIMIT", 
						"LOWERDOSELIMIT", "INFUSIONLIMIT", 
						"DADMPD", "TADMPD: TIME", "TADMBPD: TIME", 
						"DADMPS", "TADMPS: TIME", "TADMBPS: TIME", 
						"QTDBASAL"}));
		
		return insulinPumpDetailedDAO.updateValueOfParametersInsulinPump(insulinPumpDetailed, parametersName, 
				insulinPumpDetailed.getListBasal());
	}
}
