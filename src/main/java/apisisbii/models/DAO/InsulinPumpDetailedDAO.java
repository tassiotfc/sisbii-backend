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

import apisisbii.models.detailedinsulinpump.InsulinPumpDetailed;
public class InsulinPumpDetailedDAO {
	
	public static final String inputFileCPN = "/home/tassio/1/Pump-AccuV2.cpn";
	public static final String outputFileCPN = "/home/tassio/1/Pump-AccuV2.cpn";
	public static final String fileBasal = "/home/tassio/1/BasalV2AC.txt";
	
	public List<Float> getValueOfParametersInsulinPump(ArrayList<String> parametersName) throws Exception{	
		List<Float> parametersValue = new ArrayList<Float>();
		for (String parameterName : parametersName) {
			parametersValue.add(getValueOfParameter(parameterName));
		}
		return parametersValue;
	}
	
	public float getValueOfParameter(String parameterName) throws Exception{
		float parameterValue;
		Document doc = DocumentBuilderFactory.newInstance()
	            .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		int i0, i1;
		NodeList node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+parameterName+"')]", 
					doc, XPathConstants.NODESET);
		i0 = node.item(0).getTextContent().indexOf("=");
		i1 = node.item(0).getTextContent().indexOf(";");
		parameterValue = Float.parseFloat(node.item(0).getTextContent().substring(i0+2, i1));
		
		return parameterValue;
	}
	
	public List<Float> readFileBasal() throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(fileBasal));
        List<Float> valuesBasal = new ArrayList<Float>();
        String value;
        while (true) {
        	value = buffRead.readLine();
        	if (value == null) {
            	break;
            }
            
            valuesBasal.add(Float.parseFloat(value));
        }
        buffRead.close();
        return valuesBasal;
	}
	
	private boolean parameterIsInteger(String parameter) {
		if(parameter.equals("TADMPD: TIME") || parameter.equals("TADMBPD: TIME") ||
		   parameter.equals("TADMPS: TIME") || parameter.equals("TADMBPS: TIME") ||
		   parameter.equals("QTDBASAL")){
			return true;
		}
		return false;
	}

	public boolean updateValueOfParametersInsulinPump(InsulinPumpDetailed insulinPumpDetailed, 
			ArrayList<String> parametersName, List<Float> listBasal){
		try {
			Document doc = DocumentBuilderFactory.newInstance()
		        .newDocumentBuilder().parse(new InputSource(inputFileCPN));
		
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList node;
	
			List<Float> parametersValue = insulinPumpAbstractToList(insulinPumpDetailed);
					
			int index;
			index = 0;
			for (String parameterName : parametersName) {
				node = (NodeList)xpath.evaluate("//ml[contains(text(),'val "+parameterName+"')]", 
				doc, XPathConstants.NODESET);
				Node no = node.item(0);
				Element e = (Element) no;
				if(parameterIsInteger(parameterName)) {
					int parameterValue = (int) Float.parseFloat(parametersValue.get(index++).toString());
					e.getChildNodes().item(0).setTextContent("val "+parameterName+" = "
							+parameterValue+";\n          ");
				}
				else {
					e.getChildNodes().item(0).setTextContent("val "+parameterName+" = "
							+parametersValue.get(index++)+";\n          ");
				}
			}
		
			index = 0;
			for (String parameterName : parametersName) {
				node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+parameterName+"')]", 
						doc, XPathConstants.NODESET);
				
				if(parameterIsInteger(parameterName)) {
					int parameterValue = (int) Float.parseFloat(parametersValue.get(index++).toString()) ;
					node.item(0).setTextContent("val "+parameterName+" = "+parameterValue+";");
				}
				else {
					node.item(0).setTextContent("val "+parameterName+" = "+parametersValue.get(index++)+";");
				}
				
			}
		
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFileCPN)));
		
			writeFileBasal(fileBasal, listBasal);

			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private List<Float> insulinPumpAbstractToList(InsulinPumpDetailed insulinPumpDetailed){
		List<Float> parametersValue = new ArrayList<Float>();
		
		parametersValue.add(insulinPumpDetailed.getBasal());
		parametersValue.add(insulinPumpDetailed.getBolus());
		parametersValue.add(insulinPumpDetailed.getCbolus());
		parametersValue.add(insulinPumpDetailed.getCartridgeCapacity());
		parametersValue.add(insulinPumpDetailed.getUpperDoseLimit());
		parametersValue.add(insulinPumpDetailed.getLowerDoseLimit());
		parametersValue.add(insulinPumpDetailed.getInfusionLimit());
		parametersValue.add(insulinPumpDetailed.getDoseOfRateAdmSD());
		parametersValue.add((float) insulinPumpDetailed.getTimeOfRateAdmSD());
		parametersValue.add((float) insulinPumpDetailed.getTimeOfRateAdmBasalSD());
		parametersValue.add(insulinPumpDetailed.getDoseOfRateAdmPZ());
		parametersValue.add((float) insulinPumpDetailed.getTimeOfRateAdmPZ());
		parametersValue.add((float) insulinPumpDetailed.getTimeOfRateAdmBasalPZ());
		parametersValue.add((float) insulinPumpDetailed.getAmountBasal());
		return parametersValue;
	}
	
	private void writeFileBasal(String path, List<Float> listaBasal) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        for (Float integer : listaBasal) {
			buffWrite.append(integer.toString() + "\n");
		}
        buffWrite.close();
	}	
}
