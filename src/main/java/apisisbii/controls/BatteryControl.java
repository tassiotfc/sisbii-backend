package apisisbii.controls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.InstancePrinter;
import org.cpntools.accesscpn.engine.highlevel.checker.Checker;
import org.cpntools.accesscpn.engine.highlevel.checker.ErrorInitializingSMLInterface;
import org.cpntools.accesscpn.model.ModelPrinter;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.importer.DOMParser;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.xpath.XPathExpression;
import org.xml.sax.SAXException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.models.DAO.BatteryDAO;
import apisisbii.models.DAO.InsulinPumpDetailedDAO;

/**
 * @author tassio
 *
 */
@RestController
@RequestMapping(value="battery")
public class BatteryControl {
	
	/*
	 * "DONE" ""
	 */
	public final String ID_PAG_HARDWARE = "ID1436919045";
	public final String ID_INST_TRANS_HARDWARE = "ID1436919090";
	
	/**
	 * 
	 * @param number
	 * @return status (false = no done; true = done)
	 * @throws Exception
	 */
	@PostMapping("/add")
	public boolean addBatteryInstance(@RequestBody int number) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document doc;
		doc = builder.parse(InsulinPumpDetailedDAO.inputFileCPN);
		
		boolean status =  false;
		
		Element nodePageHardware = doc.getElementById(ID_PAG_HARDWARE);
		NodeList nodesTrans = nodePageHardware.getElementsByTagName("trans");
		
		BatteryDAO batteryDAO = new BatteryDAO();
		int numberBatteryInstance =  batteryDAO.countBatteryIntance(nodesTrans);
		if(numberBatteryInstance >= 1) {
			int i;
			for(i = 1; i <= number; i++) {
				batteryDAO.readXMLIDs(doc);
				Node nodeBatteryTrans = batteryDAO.getNodeBatteryTrans(nodesTrans);
				Node nodeNewBatteryTrans = nodeBatteryTrans.cloneNode(true);
				String IDNewBatteryTrans = batteryDAO.configureNewBatteryTrans(nodeNewBatteryTrans, nodePageHardware, 
						nodeBatteryTrans, numberBatteryInstance);
				
				NodeList nodesArc = nodePageHardware.getElementsByTagName("arc");
				String IDBattery = nodeBatteryTrans.getAttributes().getNamedItem("id").getNodeValue();
				List<Node> nodesBatteryArc = batteryDAO.getNodesBatteryArcs(nodesArc, IDBattery);
				batteryDAO.configureNewsBatteryArcs(nodesBatteryArc, nodePageHardware, IDNewBatteryTrans);
				
				
				Element nodeInstanceHardware = doc.getElementById(ID_INST_TRANS_HARDWARE);
				Node childNodeInstanceHardware = nodeInstanceHardware.getChildNodes().item(1);
				Node newChildNodeInstanceHardware = childNodeInstanceHardware.cloneNode(true);
				newChildNodeInstanceHardware.getAttributes().getNamedItem("id").setTextContent(batteryDAO.generateNewID());
				newChildNodeInstanceHardware.getAttributes().getNamedItem("trans").setTextContent(IDNewBatteryTrans);
				
				nodeInstanceHardware.insertBefore(newChildNodeInstanceHardware, childNodeInstanceHardware);
				
				batteryDAO.saveXML(doc, InsulinPumpDetailedDAO.outputFileCPN);
				status = true;
			}
		}
		return status;
	}
}
