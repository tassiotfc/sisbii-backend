package apisisbii.models.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import apisisbii.models.abstractinsulinpump.InsulinPumpAbstract;

public class InsulinPumpAbstractDAO {
	
	public static final String inputFileCPN = "/home/tassio/1/Pump-AccuV1.cpn";
	public static final String outputFileCPN = "/home/tassio/1/Pump-AccuV1.cpn";
	public static final String fileBasal = "/home/tassio/1/BasalV1AC.txt";
	
	public List<Integer> getValueOfParametersInsulinPump(ArrayList<String> parametersName) throws Exception{	
		List<Integer> parametersValue = new ArrayList<Integer>();
		for (String parameterName : parametersName) {
			parametersValue.add(getValueOfParameter(parameterName));
		}
		return parametersValue;
	}
	
	public int getValueOfParameter(String parameterName) throws Exception{
		int parameterValue;
		Document doc = DocumentBuilderFactory.newInstance()
	            .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		int i0, i1;
		NodeList node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+parameterName+"')]", 
					doc, XPathConstants.NODESET);
		i0 = node.item(0).getTextContent().indexOf("=");
		i1 = node.item(0).getTextContent().indexOf(";");
		parameterValue = Integer.parseInt(node.item(0).getTextContent().substring(i0+2, i1));
		return parameterValue;
	}
	
	public List<Integer> readFileBasal() throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(fileBasal));
        List<Integer> valuesBasal = new ArrayList<Integer>();
        String value;
        while (true) {
        	value = buffRead.readLine();
        	if (value == null) {
            	break;
            }
            
            valuesBasal.add(Integer.parseInt(value));
        }
        buffRead.close();
        return valuesBasal;
	}
	
	public boolean updateValueOfParametersInsulinPump(InsulinPumpAbstract insulinPumpAbstract, 
			ArrayList<String> parametersName, List<Integer> listBasal){
		try {
			Document doc = DocumentBuilderFactory.newInstance()
		        .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList node;
	
			List<Integer> parametersValue = insulinPumpAbstractToList(insulinPumpAbstract);
			
			int index;
			index = 0;
			for (String parameterName : parametersName) {
				node = (NodeList)xpath.evaluate("//ml[contains(text(),'val "+parameterName+"')]", 
				doc, XPathConstants.NODESET);
				Node no = node.item(0);
				Element e = (Element) no;
				e.getChildNodes().item(0).setTextContent("val "+parameterName+" = "
						+parametersValue.get(index++)+";\n          ");
			}
		
			index = 0;
			for (String paramaterName : parametersName) {
				node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+paramaterName+"')]", 
						doc, XPathConstants.NODESET);
				node.item(0).setTextContent("val "+paramaterName+" = "+parametersValue.get(index++)+";");
			}
		
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFileCPN)));
		
			writeFileBasal(fileBasal, listBasal);

			return true;
		}catch(Exception e) {
			
		}
		return false;
	}
	
	private List<Integer> insulinPumpAbstractToList(InsulinPumpAbstract insulinPumpAbstract){
		List<Integer> parametersValue = new ArrayList<Integer>();
		parametersValue.add(insulinPumpAbstract.getBolus());
		parametersValue.add(insulinPumpAbstract.getCbolus());
		parametersValue.add(insulinPumpAbstract.getCartridgeCapacity());
		parametersValue.add(insulinPumpAbstract.getUpperDoseLimit());
		parametersValue.add(insulinPumpAbstract.getLowerDoseLimit());
		parametersValue.add(insulinPumpAbstract.getInfusionLimit());
		parametersValue.add(insulinPumpAbstract.getBasal());
		parametersValue.add(insulinPumpAbstract.getAmountBasal());
		return parametersValue;
	}
	
	private void writeFileBasal(String path, List<Integer> listaBasal) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        for (Integer integer : listaBasal) {
			buffWrite.append(integer.toString() + "\n");
		}
        buffWrite.close();
	}	
}
