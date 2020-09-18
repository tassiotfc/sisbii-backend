package apisisbii.modelos.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import apisisbii.modelos.CPNDetalhado;
import apisisbii.modelos.CPNDetalhadoCompleto;
import apisisbii.modelos.CPNDetalhadoPadrao;
import apisisbii.modelos.CPNDetalhadoPersonalizado;

public class CPNDetalhadoDAO {
	private final String inputFileCPN = "/home/tassio/1/Pump-AccuV2.cpn";
	private final String outputFileCPN = "/home/tassio/cpnshared/Versão2/MR/Pump-MRV2.cpn";
	private final String inputFileBasal = "/home/tassio/cpnshared/Versão2/MR/BasalV2MR.txt";;
	
	public List<Float> getParametrosComunsCPNDet() throws Exception{
		List<Float> valoresParametros = new ArrayList<Float>();
		
		ArrayList<String> nomeParametros = new ArrayList<String>();
		nomeParametros.addAll(Arrays.asList(new String[]{"BOLUS", "CBOLUS", "CAPCART", 
				"UPPERDOSELIMIT", "LOWERDOSELIMIT", "INFUSIONLIMIT", "QTDBASAL"}));
		
		for (String nomeParametro : nomeParametros) {
			valoresParametros.add(getParametroCPNDet(nomeParametro));
		}
		return valoresParametros;
	}
	
	public List<Float> getParametrosTaxaCPNDet(CPNDetalhado CPNDet) throws Exception{
		List<Float> valoresParametros = new ArrayList<Float>();
		ArrayList<String> nomeParametros = new ArrayList<String>();
		if(CPNDet instanceof CPNDetalhadoPadrao) {
			nomeParametros.addAll(Arrays.asList(new String[]{"DADMPD", "TADMPD: TIME", "TADMBPD: TIME"}));	
		}
		else if(CPNDet instanceof CPNDetalhadoPersonalizado) {
			nomeParametros.addAll(Arrays.asList(new String[]{"DADMPS", "TADMPS: TIME", "TADMBPS: TIME"}));
		}
		
		for (String nomeParametro : nomeParametros) {
			valoresParametros.add(getParametroCPNDet(nomeParametro));
		}
	
		return valoresParametros;
	}
	
	public float getParametroCPNDet(String nomeParametro) throws Exception{
		float valorParametro;
		Document doc = DocumentBuilderFactory.newInstance()
	            .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		int i0, i1;
		NodeList node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+nomeParametro+"')]", 
					doc, XPathConstants.NODESET);
		i0 = node.item(0).getTextContent().indexOf("=");
		i1 = node.item(0).getTextContent().indexOf(";");
		valorParametro = Float.parseFloat(node.item(0).getTextContent().substring(i0+2, i1));
		
		return valorParametro;
	}
	
	public List<Float> getParametroBasalCPNDet(CPNDetalhado CPNDet) throws Exception{
		List<Float> valoresBasal = new ArrayList<Float>();
		if(CPNDet instanceof CPNDetalhadoPadrao) {
			valoresBasal.add(getParametroCPNDet("BASAL"));
		}
		else if(CPNDet instanceof CPNDetalhadoPersonalizado) {
			valoresBasal = leitorArquivo(inputFileBasal);
		}
		
		return valoresBasal;
	}
	
	public List<Float> leitorArquivo(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        List<Float> valoresBasal = new ArrayList<Float>();
        String linha;
        while (true) {
        	linha = buffRead.readLine();
        	if (linha == null) {
            	break;
            }
            
            valoresBasal.add(Float.parseFloat(linha));
        }
        buffRead.close();
        return valoresBasal;
	}
	
	public void criarCPNDetalhado(List<Float> valoresParametros, List<Float> valoresTaxaAdm,
			CPNDetalhado CPNDet, List<Float> valoresBasal) {
		CPNDet.setBolus(valoresParametros.get(0));
		CPNDet.setCbolus(valoresParametros.get(1));
		CPNDet.setCapCartucho(valoresParametros.get(2));
		CPNDet.setLimiteDoseSuperior(valoresParametros.get(3));
		CPNDet.setLimiteDoseInferior(valoresParametros.get(4));
		CPNDet.setLimiteInfusao(valoresParametros.get(5));
		CPNDet.setQtdBasal((int) Float.parseFloat(valoresParametros.get(6).toString()));
		if(CPNDet instanceof CPNDetalhadoPadrao) {
			((CPNDetalhadoPadrao) CPNDet).setBasal(valoresParametros.get(7));
			((CPNDetalhadoPadrao) CPNDet).setDoseTaxaAdm(valoresTaxaAdm.get(0));
			((CPNDetalhadoPadrao) CPNDet).setTempoTaxaAdm((int) 
					Float.parseFloat(valoresTaxaAdm.get(1).toString()));
			((CPNDetalhadoPadrao) CPNDet).setTempoTaxaAdmBasal((int) 
					Float.parseFloat(valoresTaxaAdm.get(2).toString()));
		}
		else if(CPNDet instanceof CPNDetalhadoPersonalizado) {
			((CPNDetalhadoPersonalizado) CPNDet).setBasal(valoresBasal);
			((CPNDetalhadoPersonalizado) CPNDet).setDoseTaxaAdm(valoresTaxaAdm.get(0));
			((CPNDetalhadoPersonalizado) CPNDet).setTempoTaxaAdm((int) 
					Float.parseFloat(valoresTaxaAdm.get(1).toString()));
			((CPNDetalhadoPersonalizado) CPNDet).setTempoTaxaAdmBasal((int) 
					Float.parseFloat(valoresTaxaAdm.get(2).toString()));
		}
	}
	
	public boolean setParametrosCPNDetCompleto(List<Float> listaParametros, List<Float> listaBasal) {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
			ArrayList<String> nomeParametros = new ArrayList<String>();
			nomeParametros.addAll(Arrays.asList(new String[]{"BASAL", "BOLUS", "CBOLUS",
					"CAPCART", "UPPERDOSELIMIT", "LOWERDOSELIMIT", "INFUSIONLIMIT", "QTDBASAL", 
					"DADMPD", "TADMPD: TIME", "TADMBPD: TIME", 
					"DADMPS", "TADMPS: TIME", "TADMBPS: TIME"}));
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList node;
		
			int index;
			index = 0;
			for (String nomeParametro : nomeParametros) {
			node = (NodeList)xpath.evaluate("//ml[contains(text(),'val "+nomeParametro+"')]", 
					doc, XPathConstants.NODESET);
				Node no = node.item(0);
				Element e = (Element) no;
				e.getChildNodes().item(0).setTextContent("val "+nomeParametro+" = "
									+listaParametros.get(index++)+";\n          ");
			}
		
			index = 0;
			for (String nomeParametro : nomeParametros) {
				node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+nomeParametro+"')]", 
					doc, XPathConstants.NODESET);
				node.item(0).setTextContent("val "+nomeParametro+" = "+listaParametros.get(index++)+";");
			}
		
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFileCPN)));
		
			escritorArquivo(inputFileBasal, listaBasal);
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	public List<Float> CPNDetCompletoToListFloat(CPNDetalhadoCompleto CPNCompleto){
		List<Float> valoresParametros = new ArrayList<Float>();
		valoresParametros.add(CPNCompleto.getBasal());
		valoresParametros.add(CPNCompleto.getBolus());
		valoresParametros.add(CPNCompleto.getCbolus());
		valoresParametros.add(CPNCompleto.getCapCartucho());
		valoresParametros.add(CPNCompleto.getLimiteDoseSuperior());
		valoresParametros.add(CPNCompleto.getLimiteDoseInferior());
		valoresParametros.add(CPNCompleto.getLimiteInfusao());
		valoresParametros.add((float) CPNCompleto.getQtdBasal());
		valoresParametros.add(CPNCompleto.getDoseTaxaAdmPD());
		valoresParametros.add((float) CPNCompleto.getTempoTaxaAdmPD());
		valoresParametros.add((float) CPNCompleto.getTempoTaxaAdmBasalPD());
		valoresParametros.add(CPNCompleto.getDoseTaxaAdmPS());
		valoresParametros.add((float) CPNCompleto.getTempoTaxaAdmPS());
		valoresParametros.add((float) CPNCompleto.getTempoTaxaAdmBasalPS());
		return valoresParametros;
	}
	
	private void escritorArquivo(String path, List<Float> listaBasal) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        for (Float valor : listaBasal) {
			buffWrite.append(valor.toString() + "\n");
		}
        buffWrite.close();
	}
}
