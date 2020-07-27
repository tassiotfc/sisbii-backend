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

import apisisbii.modelos.CPNAbstrato;
import apisisbii.modelos.CPNAbstratoCompleto;
import apisisbii.modelos.CPNAbstratoPadrao;
import apisisbii.modelos.CPNAbstratoPersonalizado;

public class CPNAbstratoDAO {
	
	private final String inputFileCPN = "/home/tassio/cpnshared/Versão1/MR/Pump-MRV1.cpn";
	private final String outputFileCPN = "/home/tassio/cpnshared/Versão1/MR/Pump-MRV1.cpn";
	private final String inputFileBasal = "/home/tassio/cpnshared/Versão1/MR/BasalV1MR.txt";
	
	public List<Integer> getParametrosComunsCPNAbst() throws Exception{
		List<Integer> valoresParametros = new ArrayList<Integer>();
		
		ArrayList<String> nomeParametros = new ArrayList<String>();
		nomeParametros.addAll(Arrays.asList(new String[]{"BOLUS", "CBOLUS", "CAPCART", 
				"UPPERDOSELIMIT", "LOWERDOSELIMIT", "INFUSIONLIMIT", "QTDBASAL"}));
		for (String nomeParametro : nomeParametros) {
			valoresParametros.add(getParametroCPNAbst(nomeParametro));
		}
		return valoresParametros;
	}
	
	public int getParametroCPNAbst(String nomeParametro) throws Exception{
		int valorParametro;
		Document doc = DocumentBuilderFactory.newInstance()
	            .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		int i0, i1;
		NodeList node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+nomeParametro+"')]", 
					doc, XPathConstants.NODESET);
		i0 = node.item(0).getTextContent().indexOf("=");
		i1 = node.item(0).getTextContent().indexOf(";");
		valorParametro = Integer.parseInt(node.item(0).getTextContent().substring(i0+2, i1));
		
		return valorParametro;
	}
	
	public List<Integer> getParametroBasalCPNAbst(CPNAbstrato CPNAbst) throws Exception{
		List<Integer> valoresBasal = new ArrayList<Integer>();
		if(CPNAbst instanceof CPNAbstratoPadrao) {
			valoresBasal.add(getParametroCPNAbst("BASAL"));
		}
		else if(CPNAbst instanceof CPNAbstratoPersonalizado) {
			valoresBasal = leitorArquivo(inputFileBasal);
		}
		
		return valoresBasal;
	}
	
	public List<Integer> leitorArquivo(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        List<Integer> valoresBasal = new ArrayList<Integer>();
        String linha;
        while (true) {
        	linha = buffRead.readLine();
        	if (linha == null) {
            	break;
            }
            
            valoresBasal.add(Integer.parseInt(linha));
        }
        buffRead.close();
        return valoresBasal;
	}
	
	public void criarCPNAbstrato(List<Integer> valoresParametros, CPNAbstrato CPNAbst,
									List<Integer> valoresBasal) {
		CPNAbst.setBolus(valoresParametros.get(0));
		CPNAbst.setCbolus(valoresParametros.get(1));
		CPNAbst.setCapCartucho(valoresParametros.get(2));
		CPNAbst.setLimiteDoseSuperior(valoresParametros.get(3));
		CPNAbst.setLimiteDoseInferior(valoresParametros.get(4));
		CPNAbst.setLimiteInfusao(valoresParametros.get(5));
		CPNAbst.setQtdBasal(valoresParametros.get(6));
		if(CPNAbst instanceof CPNAbstratoPadrao) {
			((CPNAbstratoPadrao) CPNAbst).setBasal(valoresParametros.get(7));
		}
		else if(CPNAbst instanceof CPNAbstratoPersonalizado) {
			((CPNAbstratoPersonalizado) CPNAbst).setBasal(valoresBasal);
		}
	}
	
	public boolean setParametrosCPNAbstCompleto(List<Integer> listaParametros, List<Integer> listaBasal) {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
			ArrayList<String> nomeParametros = new ArrayList<String>();
			nomeParametros.addAll(Arrays.asList(new String[]{"BASAL", "BOLUS", "CBOLUS", "CAPCART", 
					"UPPERDOSELIMIT", "LOWERDOSELIMIT", "INFUSIONLIMIT", "QTDBASAL"}));
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
	
	public List<Integer> CPNAbstCompletoToListInt(CPNAbstratoCompleto CPNCompleto){
		List<Integer> valoresParametros = new ArrayList<Integer>();
		valoresParametros.add(CPNCompleto.getBasal());
		valoresParametros.add(CPNCompleto.getBolus());
		valoresParametros.add(CPNCompleto.getCbolus());
		valoresParametros.add(CPNCompleto.getCapCartucho());
		valoresParametros.add(CPNCompleto.getLimiteDoseSuperior());
		valoresParametros.add(CPNCompleto.getLimiteDoseInferior());
		valoresParametros.add(CPNCompleto.getLimiteInfusao());
		valoresParametros.add(CPNCompleto.getQtdBasal());
		return valoresParametros;
	}
	
	private void escritorArquivo(String path, List<Integer> listaBasal) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        for (Integer integer : listaBasal) {
			buffWrite.append(integer.toString() + "\n");
		}
        buffWrite.close();
	}
	
}
