package apisisbii.modelos.DAO;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.InstancePrinter;
import org.cpntools.accesscpn.engine.highlevel.checker.Checker;
import org.cpntools.accesscpn.engine.highlevel.checker.ErrorInitializingSMLInterface;
import org.cpntools.accesscpn.engine.highlevel.instance.Binding;
import org.cpntools.accesscpn.engine.highlevel.instance.Instance;
import org.cpntools.accesscpn.engine.highlevel.instance.adapter.ModelInstance;
import org.cpntools.accesscpn.engine.highlevel.instance.adapter.ModelInstanceAdapterFactory;
import org.cpntools.accesscpn.engine.highlevel.instance.cpnvalues.CPNValue;
import org.cpntools.accesscpn.model.HLDeclaration;
import org.cpntools.accesscpn.model.ModelPrinter;
import org.cpntools.accesscpn.model.Page;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.Place;
import org.cpntools.accesscpn.model.PlaceNode;
import org.cpntools.accesscpn.model.Transition;
import org.cpntools.accesscpn.model.declaration.DeclarationStructure;
import org.cpntools.accesscpn.model.declaration.impl.DeclarationStructureImpl;
import org.cpntools.accesscpn.model.impl.HLDeclarationImpl;
import org.cpntools.accesscpn.model.importer.DOMParser;
import org.cpntools.accesscpn.model.importer.NetCheckException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import apisisbii.modelos.OpcoesSimulacao;
import apisisbii.modelos.Simulador;

import org.cpntools.accesscpn.engine.highlevel.instance.Instance;

public class SimuladorDAO {
	private final String inputFileCPN = "/home/tassio/0/Pump-AccuV2.cpn";
	HighLevelSimulator s;
	Simulador simulador;
	PetriNet petriNet;
	CPNDetalhadoDAO CPNDetDAO = new CPNDetalhadoDAO();
	
	private void iniciaSimulador() throws Exception{
		if(s != null) {
			s.destroy();
		}
		petriNet = DOMParser.parse(new URL("file://" + inputFileCPN));
		s = HighLevelSimulator.getHighLevelSimulator();
		try {
			s.setSimulationReportOptions(false, false, "");
			final Checker checker = new Checker(petriNet, null, s);
			try {
				checker.checkEntireModel("/home/tassio/0", "/home/tassio/0");
			} catch (final ErrorInitializingSMLInterface e) {
				// Ignore
			}
		}
		finally {
			System.out.println("Novo simulador criado!");
		}
	}
	
	public Simulador getDadosTelaInicialDaBomba() throws Exception {
		iniciaSimulador();
		simulador = new Simulador();
		configParametrosConstantes();
		configQtdBateriaCarregada();
		configQtdBateriaDescarregada();
		configTempoAtual();
		configTransicoesHabilitadasEDisparada(Arrays.asList(new String[]{"System.Hardware.Start_Button"}));
		return simulador;
	}
	
	public void configParametrosConstantes() throws Exception{
		simulador.setBasal(CPNDetDAO.getParametroCPNDet("BASAL"));
		simulador.setBolus(CPNDetDAO.getParametroCPNDet("BOLUS"));
		simulador.setCbolus(CPNDetDAO.getParametroCPNDet("CBOLUS"));
		simulador.setCapCartucho(CPNDetDAO.getParametroCPNDet("CAPCART"));
		simulador.setDoseTaxaAdm(CPNDetDAO.getParametroCPNDet("DADMPS"));
		simulador.setTempoTaxaAdm((int) CPNDetDAO.getParametroCPNDet("TADMPS: TIME"));
	}
	
	private void configQtdBateriaCarregada() throws Exception{
		int contBat = 0;
		for(Instance<PlaceNode> pi: s.getAllPlaceInstances()) {
			if(pi.toString().contains("Battery") && pi.toString().contains("Value") &&
					s.getMarking(pi).toString().contains("1`1")) {
				contBat++;
			}
		}
		simulador.setQtdBatCarregada(contBat);
	}
	
	private void configQtdBateriaDescarregada() throws Exception{
		int contBat = 0;
		for(Instance<PlaceNode> pi: s.getAllPlaceInstances()) {
			if(pi.toString().contains("Battery") && pi.toString().contains("Value") && 
					(s.getMarking(pi).toString().contains("1`0") || s.getMarking(pi).toString().contains("1`2"))) {
				contBat++;
			}
		}
		simulador.setQtdBatDescarregada(contBat);
	}
	
	private void configTempoAtual() throws Exception{
		simulador.setTempoAtual(Integer.parseInt(s.getTime()));
	}
	
	private void configTransicoesHabilitadasEDisparada(List<String> transicoesDeParada) throws IOException, Exception{
		if(transicoesDeParada != null) {
			List<String> transicoes = new ArrayList<String>();
			String ultimaTransicaoDisparada = simulador.getTransicaoDisparada();
		List<Instance<? extends Transition>> tis;
			while(true) {
			tis = s.isEnabled(s.getAllTransitionInstances());
			int cont = 0;
			for(String transicao: transicoesDeParada) {
				for (Instance<? extends Transition> instance : tis) {
					if(instance.toString().contains(transicao)) {
						cont++;
					}
				}
			}
				if(cont == transicoesDeParada.size()) {
					for (Instance<? extends Transition> instance : tis) {
						transicoes.add(instance.toString());
					}
					break;
				}
				Instance<? extends Transition> instance = s.execute();
				ultimaTransicaoDisparada = instance.toString();
		}
		simulador.setTransicoesHabilitadas(transicoes);
		simulador.setTransicaoDisparada(ultimaTransicaoDisparada);
		}
	}
	
	//Cartridge.Cap_Cart
	private void configVolumeCartucho() throws Exception {
		simulador.setVolCartucho(getValorLugar("Cartridge.Cap_Cart"));
	}
	
	private float getValorLugar(String lugar) throws Exception{
		float valorLugar = -1;
		int i0, i1;
		for(Instance<PlaceNode> pi: s.getAllPlaceInstances()) {
			if(pi.toString().contains(lugar)) {
				String aux = s.getMarking(pi).toString();
				i0 = aux.indexOf("`");
				i1 = aux.length();
				valorLugar = Float.parseFloat(aux.substring(i0+1, i1));
				break;
			}
		}
		return valorLugar;
	}
	
	//
	private Instance<? extends Transition> getTransicaoHabilitada(String transicao) throws Exception{
		List<Instance<? extends Transition>> tis = s.isEnabled(s.getAllTransitionInstances());
		Instance<? extends Transition> ti = null;
		for (Instance<? extends Transition> instance : tis) {
			if(instance.toString().contains(transicao)) {
				ti = instance;
				return ti;
			}
		}
		return ti;
	}
	
	public boolean disparaTransicao(String transicao) throws Exception{
		
		Instance<? extends Transition> ti = getTransicaoHabilitada(transicao);
		if(ti != null) {
			s.execute(ti);
			System.out.println("222");
			simulador.setTransicoesHabilitadas(transicoesHabilitadasEmString(s.isEnabled(s.getAllTransitionInstances())));
			simulador.setTransicaoDisparada(ti.toString());
			System.out.println("333");
			return true;
		}
		return false;
	}
	
	private List<String>  transicoesHabilitadasEmString(List<Instance<? extends Transition>> tis) 
			throws IOException, Exception{
		
		List<String> transicoes = new ArrayList<String>();
		for (Instance<? extends Transition> instance : tis) {
			transicoes.add(instance.toString());
		}
		return transicoes;
	}
	
	//SMC.Get_Cap_Cartridge PMC.Get_Cap_Cartridge
	public Simulador getDadosTelaSelecaoBomba(OpcoesSimulacao opSim) throws Exception{
		simulador.setOpcoesSimulacao(opSim);
		List<String> transicoesDeParada = new ArrayList<String>();
		if(simulador.getOpcoesSimulacao().getModoBasal().equals("padrao")) {
			configTransicoesHabilitadasEDisparada(Arrays.asList(new String[]{"SMC.Standard_Conf"}));
			simulaTransicao("SMC.Standard_Conf");
			transicoesDeParada.addAll(Arrays.asList(new String[]{"SI.Adm_Bolus", "SI.Adm_CBolus"}));
		}
		else if(simulador.getOpcoesSimulacao().getModoBasal().equals("personalizado")) {
			configTransicoesHabilitadasEDisparada(Arrays.asList(new String[]{"PMC.Basal_Read_Conf"}));
			simulaTransicao("PMC.Basal_Read_Conf");
			transicoesDeParada.addAll(Arrays.asList(new String[]{"PI.Adm_Bolus", "PI.Adm_CBolus"}));
		}
		configParametrosVariaveis(transicoesDeParada);
		return simulador;
	}
	
	/*
	 * 0 basal					Adm_Basal
	 * 1 bolus					Adm_Bolus
	 * 2 cbolus					Adm_CBolus
	 * 3 aplicar				Apply_Insulin	
	 * 4 recarregar_bateria		Recharge_Cart
	 * 5 encerrar_recarga		
	 * 6 error1 error_tempo_selecao		Critical_Error1 (nenhuma selecionada)
	 * 7 error2 error_tempo_selecao 	Critical_Error2 (pelo menos uma foi selecionada)
	 * 8 error3 error_tempo_infusao		Critical_Error3 (momento da infusao)
	 */
	/*
	 * livre
	 * error_tempo_selecao
	 * error_tempo_aplicacao
	 * baterias_descarregadas
	 */
	public Simulador getDadosTelaSelecaoBomba(String transicao) throws Exception{
		List<String> transicoesDeParada = new ArrayList<String>();
		
		if(simulador.getOpcoesSimulacao().getModoBasal().equals("padrao")) {
			transicoesDeParada.addAll(Arrays.asList(new String[]{"SI.Adm_Basal", "SI.Adm_Bolus", "SI.Adm_CBolus",
			"SI.Apply_Insulin", "SI.Critical_Error1", "SI.Critical_Error2", "SI.Critical_Error3"}));
			if(simulador.getOpcoesSimulacao().getEstadoFinalDesejado().equals("livre")) {
				if(transicao.equals("basal")) {
					
				}
				else if(transicao.equals("bolus")) {
					
				}
				else if(transicao.equals("cbolus")) {
					
				}
				else if(transicao.equals("aplicar")) {
					
				}
			}
			else if(simulador.getOpcoesSimulacao().getEstadoFinalDesejado().equals("error_tempo_selecao")) {
				if(transicao.equals("basal") || transicao.equals("bolus") || transicao.equals("cbolus")) {
					if(disparaTransicao("SI.Adm_Basal") || disparaTransicao("SI.Adm_Bolus") ||
							disparaTransicao("SI.Adm_CBolus")) {
						disparaTransicao("SI.Critical_Error2");
						disparaTransicao("VB.Notify_Bug");
					}
				}
				else if(transicao.equals("aplicar")){
					if(disparaTransicao("SI.Apply_Insulin")) {
						disparaTransicao("SI.Critical_Error1");
					}
				}
			}
			else if(simulador.getOpcoesSimulacao().getEstadoFinalDesejado().equals("error_tempo_infusao")) {
				
			}
			else if(simulador.getOpcoesSimulacao().getEstadoFinalDesejado().equals("baterias_descarregadas")) {
				
			}
		}
		else if(simulador.getOpcoesSimulacao().getModoBasal().equals("personalizado")) {
			transicoesDeParada.addAll(Arrays.asList(new String[]{"PI.Adm_Basal", "PI.Adm_Bolus", "PI.Adm_CBolus",
			"PI.Apply_Insulin"}));
		}
		return simulador;
	}
	
	private void simulaTransicao(String transicao) throws Exception{
		List<Instance<? extends Transition>> tis = s.isEnabled(s.getAllTransitionInstances());
		for (Instance<? extends Transition> instance : tis) {
			if(instance.toString().contains(transicao)) {
				s.execute(instance);
				break;
			}
		}
	}
	
	//SI.DTA ou PI.DTA SI.LA PI.LA
	private void configParametrosVariaveis(List<String> transicoesDeParada) throws Exception{
		configQtdBateriaCarregada();
		configQtdBateriaDescarregada();
		configTempoAtual();
		configVolumeCartucho();
		configTransicoesHabilitadasEDisparada(transicoesDeParada);
	}
	
	public boolean destruirSimulador() {
		if(s != null) {
			s.destroy();
			return true;
		}
		return false;
	}
	
	private List<Instance<Transition>> transicoesHabilitadasToExecutaveis
	(List<Instance<? extends Transition>> tisEnabled) throws Exception{
		List<Instance<Transition>> tisExecutaveis = new ArrayList<Instance<Transition>>();
		for (Instance<? extends Transition> instance : tisEnabled) {
			tisExecutaveis.add((Instance<Transition>) instance);
		}
		return tisExecutaveis;
	}
	
	
//	private void configParametrosVariaveis(List<String> transicoesDeParada) throws Exception{
//	configQtdBateriaCarregada();
//	configQtdBateriaDescarregada();
//	configTempoAtual();
//	configVolumeCartucho();
//	if(simulador.getOpcoesSimulacao().getModoBasal().equals("Padrão")) {
//		configTransicoesHabilitadasEDisparada(Arrays.asList(new String[]{"SI.Adm_Bolus", "SI.Adm_CBolus"}));
//	}
//	else if(simulador.getOpcoesSimulacao().getModoBasal().equals("Personalizado")) {
//		configTransicoesHabilitadasEDisparada(Arrays.asList(new String[]{"PI.Adm_Bolus", "PI.Adm_CBolus"}));
//	}
//}

//ArrayList<String> nomeParametros = new ArrayList<String>();
//nomeParametros.addAll(Arrays.asList(new String[]{"BASAL", "BOLUS", "CBOLUS",
//		"CAPCART", "UPPERDOSELIMIT", "LOWERDOSELIMIT", "INFUSIONLIMIT", "QTDBASAL", 
//		"DADMPD", "TADMPD: TIME", "TADMBPD: TIME", 
//		"DADMPS", "TADMPS: TIME", "TADMBPS: TIME"}));
	
	
	
	
//	private boolean lugarModificado(String lugar, String valorInicial) throws Exception {
//	boolean aux = false;
//	for(Instance<PlaceNode> pi: s.getAllPlaceInstances()) {
//		if(pi.toString().contains(lugar)) {
//			aux = s.getMarking(pi).toString().contains(valorInicial) ? true: false;
//			break;
//		}
//	}
//	return aux;
//}
}
