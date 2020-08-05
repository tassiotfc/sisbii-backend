package apisisbii.controladores;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.modelos.CPNDetalhadoPadrao;
import apisisbii.modelos.OpcoesSimulacao;
import apisisbii.modelos.DAO.SimuladorDAO;
import apisisbii.modelos.Simulador;

@RestController
@RequestMapping(value="api")
public class CPNSimuladorControlador {
	/*
	 * Na simulacao livre, analisar possibilidade de liberar pro usuario selecionar 
	 * no log qual transicao disparar
	 */
	
	SimuladorDAO simuladorDAO;
	
	@Autowired
	public CPNSimuladorControlador() throws Exception {
		simuladorDAO = new SimuladorDAO();
	}
	/*
	 * Chamar quando usuario:
	 * 	- Selecionar a funcionalidade de simular
	 * 	- Esta simulando e seleciona redefinir
	 * 	- Ocorreu algum estado final e ele pede pra reiniciar simulacao
	 */
	@GetMapping("/telainicial")
	public Simulador listaDadosTelaInicial() throws Exception {
		return simuladorDAO.getDadosTelaInicialDaBomba();
	}
	
	/*
	 * Chamar quando usuario esta em algum tela de simulacao e:
	 * 	Selecionar alguma opcao de sair (o nome SisBII no canto superior esquerdo)
	 */	
	@GetMapping("/encerrasimulacao")
	public boolean encerraSimulacao() throws Exception {
		return simuladorDAO.destruirSimulador();
	}
	
	/*
	 * Chamar quando o usuario seleciona:
	 * 	Basal
	 * 	Bolus Padrao
	 * 	Bolus Corretivo
	 * 	Confirmacao (aplicar, recarregar_bateria, encerrar_recarga)
	 */
	@PostMapping("/telasimulacao")
	public Simulador simula(@RequestBody String transicao) throws Exception {
		return simuladorDAO.getDadosTelaSelecaoBomba(transicao);
		//return simuladorDAO.disparaTransicao(transicao);
	}
	
	/*
	 * Chamar quando o usuario confirma opcoes de simulacao e inicia a simulacao
	 */
	@PostMapping("/telainicialselecao")
	public Simulador listaDadosTelaInicialSelecao(@RequestBody OpcoesSimulacao opcoesSimulacao) throws Exception {
		return simuladorDAO.getDadosTelaSelecaoBomba(opcoesSimulacao);
	}
}