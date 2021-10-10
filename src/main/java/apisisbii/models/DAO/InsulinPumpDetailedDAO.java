package apisisbii.models.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import apisisbii.ApirestApplication;
import apisisbii.models.detailedinsulinpump.InsulinPumpDetailed;
public class InsulinPumpDetailedDAO {
	
	private String inputFileCPN;
	private String outputFileCPN;
	private String fileBasal;
	
	public InsulinPumpDetailedDAO() {
		configPathFileInsulinPump();
		configPathFileBasal();
	}
	
	public String getFileInputFileCPN() {
		return inputFileCPN;
	}
	
	public String getFileOutputFileCPN() {
		return outputFileCPN;
	}
	
	public String getFileBasal() {
		return fileBasal;
	}
	
	private void configPathFileInsulinPump(){
	    try {
	    	File file = ResourceUtils.getFile("classpath:PumpAccuV2.cpn");
	        inputFileCPN = "" + file;
	        outputFileCPN = inputFileCPN;
	    } catch (IOException e) {
	    	e.getMessage();
	    }
    }
	
	private void configPathFileBasal(){
	    try {
	    	File file = ResourceUtils.getFile("classpath:BasalV2AC.txt");
	        fileBasal = "" + file;
	    } catch (IOException e) {
	    	e.getMessage();
	    }
    }
	
	public List<Float> getValueOfParametersInsulinPump(ArrayList<String> parametersName) throws Exception{	
		List<Float> parametersValue = new ArrayList<Float>();
		for (String parameterName : parametersName) {
			parametersValue.add(getValueOfParameter(parameterName));
		}
		return parametersValue;
	}
	
	public float getValueOfParameter(String parameterName) throws Exception{
		float parameterValue;
		Document doc = getDocumentInsulinPump();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		int i0, i1;
		NodeList node = (NodeList)xpath.evaluate("//layout[contains(text(),'val "+parameterName+"')]", 
					doc, XPathConstants.NODESET); 
		i0 = node.item(0).getTextContent().indexOf("="); 
		i1 = node.item(0).getTextContent().indexOf(";");
		parameterValue = Float.parseFloat(node.item(0).getTextContent().substring(i0+2, i1));
		
		return parameterValue;
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
			Document doc = getDocumentInsulinPump();
		
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
		
			saveInsulinPump(doc);
			saveFileBasal(fileBasal, listBasal);

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
	
	private void saveFileBasal(String path, List<Float> listaBasal) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        for (Float integer : listaBasal) {
			buffWrite.append(integer.toString() + "\n");
		}
        buffWrite.close();
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
	
	public void saveInsulinPump(Document doc) throws Exception{
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.INDENT, "no");
		xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("doctype",
		    "-//CPN//DTD CPNXML 1.0//EN",
		    "http://cpntools.org/DTD/6/cpn.dtd");
		xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
		xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
		xformer.transform(new DOMSource(doc), new StreamResult(new File(outputFileCPN)));
	}
	
	public Document getDocumentInsulinPump() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc = builder.parse(inputFileCPN);
		return doc;
	}
}
