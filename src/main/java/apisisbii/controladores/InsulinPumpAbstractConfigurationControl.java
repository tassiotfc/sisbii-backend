package apisisbii.controladores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.modelos.DAO.InsulinPumpAbstractDAO;
import apisisbii.models.abstractinsulinpump.InsulinPumpAbstract;

@RestController
@RequestMapping(value="config")
public class InsulinPumpAbstractConfigurationControl {
	
	@GetMapping("/listparametersabstract")
	public InsulinPumpAbstract listParametersInsulinPumpAbstract() throws Exception {
		InsulinPumpAbstractDAO insulinPumpAbstratDAO = new InsulinPumpAbstractDAO();
		
		ArrayList<String> parametersName = new ArrayList<String>(Arrays.asList(
				new String[]{"BOLUS", "CBOLUS", "CAPCART", "UPPERDOSELIMIT", 
						"LOWERDOSELIMIT", "INFUSIONLIMIT", "BASAL", "QTDBASAL"}));
		List<Integer> parametersValue = insulinPumpAbstratDAO.getValueOfParametersInsulinPump(parametersName);
		List<Integer> valuesBasal = insulinPumpAbstratDAO.readFileBasal();
		InsulinPumpAbstract insulinPumpAbstract = new InsulinPumpAbstract(parametersValue.get(0), 
				parametersValue.get(1), parametersValue.get(2), parametersValue.get(3),
				parametersValue.get(4),	parametersValue.get(5),	parametersValue.get(6),
				parametersValue.get(7), valuesBasal);
		return insulinPumpAbstract;
	}

	@PostMapping("/updateparametersabstract")
	public boolean updateParametersInsulinPumpAbstractStandard(
			@RequestBody InsulinPumpAbstract insulinPumpAbstract) throws Exception {
		InsulinPumpAbstractDAO insulinPumpAbstratDAO = new InsulinPumpAbstractDAO();
		
		ArrayList<String> parametersName = new ArrayList<String>(Arrays.asList(
				new String[]{"BOLUS", "CBOLUS", "CAPCART", "UPPERDOSELIMIT", 
						"LOWERDOSELIMIT", "INFUSIONLIMIT", "BASAL", "QTDBASAL"}));
		
		return insulinPumpAbstratDAO.updateValueOfParametersInsulinPump(insulinPumpAbstract, parametersName,
				insulinPumpAbstract.getListBasal());
	}
}
