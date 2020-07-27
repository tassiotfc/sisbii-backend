package apisisbii.controladores;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.modelos.CPNDetalhadoCompleto;
import apisisbii.modelos.CPNDetalhadoPadrao;
import apisisbii.modelos.CPNDetalhadoPersonalizado;
import apisisbii.modelos.DAO.CPNDetalhadoDAO;

@RestController
@RequestMapping(value="api")
public class CPNDetalhadoControlador {
	
	CPNDetalhadoDAO CPNDetDAO = new CPNDetalhadoDAO();
	
	@GetMapping("/listaparametroscpndetpd")
	public CPNDetalhadoPadrao listaParametrosCPNDetPD() throws Exception {
		CPNDetalhadoPadrao CPNDetalhadoPD = new CPNDetalhadoPadrao();
		List<Float> valoresParametros = CPNDetDAO.getParametrosComunsCPNDet();
		List<Float> valoresTaxaAdm = CPNDetDAO.getParametrosTaxaCPNDet(CPNDetalhadoPD);
		valoresParametros.add(CPNDetDAO.getParametroBasalCPNDet(CPNDetalhadoPD).get(0));
		CPNDetDAO.criarCPNDetalhado(valoresParametros, valoresTaxaAdm, CPNDetalhadoPD, null);
		return CPNDetalhadoPD;
	}
	
	@GetMapping("/listaparametroscpndetps")
	public CPNDetalhadoPersonalizado getParametrosCPNDetPS() throws Exception {
		CPNDetalhadoPersonalizado CPNDetalhadoPS = new CPNDetalhadoPersonalizado();
		List<Float> valoresParametros = CPNDetDAO.getParametrosComunsCPNDet();
		List<Float> valoresTaxaAdm = CPNDetDAO.getParametrosTaxaCPNDet(CPNDetalhadoPS);
		List<Float> valoresBasal = CPNDetDAO.getParametroBasalCPNDet(CPNDetalhadoPS);	
		CPNDetDAO.criarCPNDetalhado(valoresParametros, valoresTaxaAdm, CPNDetalhadoPS, valoresBasal);
		return CPNDetalhadoPS;
	}
	
	@PostMapping("/configparametroscpndet")
	public boolean configCPNAsbtrato(@RequestBody CPNDetalhadoCompleto CPNCompleto){	
		System.out.println(CPNCompleto.getBasal());
		List<Float> listaParametros = CPNDetDAO.CPNDetCompletoToListFloat(CPNCompleto);
		List<Float> listaBasal = CPNCompleto.getListaBasal();
		if(CPNDetDAO.setParametrosCPNDetCompleto(listaParametros, listaBasal)) {
			return true;
		}
		return false;
	}
}