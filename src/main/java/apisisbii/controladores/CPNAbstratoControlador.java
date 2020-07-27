package apisisbii.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.modelos.CPNAbstratoCompleto;
import apisisbii.modelos.CPNAbstratoPadrao;
import apisisbii.modelos.CPNAbstratoPersonalizado;
import apisisbii.modelos.DAO.CPNAbstratoDAO;

@RestController
@RequestMapping(value="api")
public class CPNAbstratoControlador {
	
	CPNAbstratoDAO CPNAbstDAO = new CPNAbstratoDAO();
	
	@GetMapping("/listaparametroscpnabstpd")
	public CPNAbstratoPadrao listaParametrosCPNAbstPD() throws Exception {
		CPNAbstratoPadrao CPNAbstratoPD = new CPNAbstratoPadrao();
		List<Integer> valoresParametros = CPNAbstDAO.getParametrosComunsCPNAbst();
		valoresParametros.add(CPNAbstDAO.getParametroBasalCPNAbst(CPNAbstratoPD).get(0));
		CPNAbstDAO.criarCPNAbstrato(valoresParametros, CPNAbstratoPD, null);
		return CPNAbstratoPD;
	}
	
	@GetMapping("/listaparametroscpnabstps")
	public CPNAbstratoPersonalizado getParametrosCPNAbstPS() throws Exception {
		CPNAbstratoPersonalizado CPNAbstratoPS = new CPNAbstratoPersonalizado();
		List<Integer> valoresParametros = CPNAbstDAO.getParametrosComunsCPNAbst();
		List<Integer> valoresBasal = CPNAbstDAO.getParametroBasalCPNAbst(CPNAbstratoPS);	
		CPNAbstDAO.criarCPNAbstrato(valoresParametros, CPNAbstratoPS, valoresBasal);
		return CPNAbstratoPS;
	}
	
	@PostMapping("/configparametroscpnabst")
	public boolean configCPNAsbtrato(@RequestBody CPNAbstratoCompleto CPNCompleto){	
		List<Integer> listaParametros = CPNAbstDAO.CPNAbstCompletoToListInt(CPNCompleto);
		List<Integer> listaBasal = CPNCompleto.getListaBasal();
		if(CPNAbstDAO.setParametrosCPNAbstCompleto(listaParametros, listaBasal)) {
			return true;
		}
		return false;
	}
}
