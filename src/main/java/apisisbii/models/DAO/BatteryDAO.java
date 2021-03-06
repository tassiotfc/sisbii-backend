package apisisbii.models.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BatteryDAO {
	private List<String> IDs = new ArrayList<String>(); 
	
	public int countBatteryIntance(NodeList nodesTrans) {
		int count = 0;
		int i;
		for(i = 0; i < nodesTrans.getLength(); i++) {
			if(nodesTrans.item(i).getTextContent().contains("Battery")) {
				count++;
			}
		}
		return count;
	}
	
	public void setBatteryInstanceQuantity(Document doc, String ID_OKB1, 
			String ID_OKB2, String ID_OKB3, int quantity) {
		Element nodeMarkingOKB1 = doc.getElementById(ID_OKB1);
		Element nodeMarkingOKB2 = doc.getElementById(ID_OKB2);
		Element nodeMarkingOKB3 = doc.getElementById(ID_OKB3);
		NodeList childsNodeMarkingOKB1 = nodeMarkingOKB1.getChildNodes();
		NodeList childsNodeMarkingOKB2 = nodeMarkingOKB2.getChildNodes();
		NodeList childsNodeMarkingOKB3 = nodeMarkingOKB3.getChildNodes();
		childsNodeMarkingOKB1.item(9).setTextContent(quantity + "`1");
		childsNodeMarkingOKB2.item(9).setTextContent(quantity + "`1");
		childsNodeMarkingOKB3.item(9).setTextContent(quantity + "`1");
	}
	
	public void readXMLIDs(Document doc) throws Exception{
		XPath xpath = XPathFactory.newInstance().newXPath();	
		NodeList nodes = (NodeList)xpath.evaluate("//@id", doc, XPathConstants.NODESET);
		
		int i;
		for(i = 0; i < nodes.getLength(); i++) {
			IDs.add(nodes.item(i).getNodeValue());
		}
	}
	
	public Node getNodeBatteryTrans(NodeList nodesTrans) {
		int i;
		for(i = 0; i < nodesTrans.getLength(); i++) {
			if(nodesTrans.item(i).getTextContent().contains("Battery")) {
				return nodesTrans.item(i);
			}
		}
		return null;
	}
	
	public String generateNewID() throws Exception{
		int i;
		boolean aux;
		int id = 1;
		while(true) {
			aux = true;
			for(i = 0; i < IDs.size(); i++) {
				if(IDs.get(i).contains("ID"+id)) {
					id++;
					aux = false;
					break;
				}
			}
			if(aux) {
				IDs.add("ID"+id);
				return "ID"+id;
			}
		}
	}
	
	public String configureNewBatteryTrans(Node nodeNewBatteryTrans, Element nodePageHardware, 
			Node nodeBatteryTrans, int numberBatteryInstance) throws Exception {
		String IDNewBatteryTrans = generateNewID();
		nodeNewBatteryTrans.getAttributes().getNamedItem("id").setNodeValue(IDNewBatteryTrans);
		
		NodeList childsNewBatteryTrans = nodeNewBatteryTrans.getChildNodes();
		
		int i;
		for(i = 0; i < childsNewBatteryTrans.getLength(); i++) {
			Node childNewBatteryTrans = childsNewBatteryTrans.item(i);
			if(childNewBatteryTrans.hasAttributes()) {
				if(childNewBatteryTrans.getAttributes().getNamedItem("id") != null) {
					childNewBatteryTrans.getAttributes().getNamedItem("id").setNodeValue(generateNewID());
				}
			}
			if(childNewBatteryTrans.getNodeName().toString().contains("subst")) {
				childNewBatteryTrans.getChildNodes().item(1).getAttributes().
						getNamedItem("id").setNodeValue(generateNewID());
			}
			if(childNewBatteryTrans.getNodeName().toString().equals("text")) {
				childNewBatteryTrans.setTextContent("Battery"+(numberBatteryInstance+1));
			}
			if(childNewBatteryTrans.getNodeName().equals("posattr")) {
				float value = Float.parseFloat(childNewBatteryTrans.getAttributes().
						getNamedItem("y").getNodeValue());
				value += 100;  
				childNewBatteryTrans.getAttributes().getNamedItem("y").setNodeValue(""+value);
			}
		}
		nodePageHardware.insertBefore(nodeNewBatteryTrans, nodeBatteryTrans);
		return IDNewBatteryTrans;
	}
	
	public List<Node> getNodesBatteryArcs(NodeList nodesArc, String IDBattery) {
		int i;
		List<Node> nodesBatteryArc = new ArrayList<Node>();
		for(i = 0; i < nodesArc.getLength(); i++) {			
			int j;
			NodeList childsArc = nodesArc.item(i).getChildNodes(); 
			for(j = 0; j < childsArc.getLength(); j++) {
				Node childArc = childsArc.item(j);
				if(childArc.getNodeName().equals("transend")) {
					if(childArc.getAttributes().getNamedItem("idref").getNodeValue().equals(IDBattery) ) {
						nodesBatteryArc.add(nodesArc.item(i));
					}
				}
			}
		}
		return nodesBatteryArc;
	}
	
	public void configureNewsBatteryArcs(List<Node> nodesBatteryArc, Element nodePageHardware, 
			String IDNewBatteryTrans) throws Exception{
		int i;
		for(i = 0; i < nodesBatteryArc.size()   ; i++) {
			Node nodeBatteryArc = nodesBatteryArc.get(i);
			Node newNodeBatteryArc = nodeBatteryArc.cloneNode(true);
			newNodeBatteryArc.getAttributes().getNamedItem("id").setNodeValue(generateNewID());
			
			NodeList childsNewNodeBatteryArc = newNodeBatteryArc.getChildNodes();
			
			int j;
			for(j = 0; j < childsNewNodeBatteryArc.getLength(); j++) {
				Node childNewNodeBatteryArc = childsNewNodeBatteryArc.item(j);
				if(childNewNodeBatteryArc.hasAttributes()) {
					if(childNewNodeBatteryArc.getAttributes().getNamedItem("id") != null) {
						childNewNodeBatteryArc.getAttributes().getNamedItem("id").setNodeValue(generateNewID());
					}
				}
				if(childNewNodeBatteryArc.getNodeName().toString().equals("transend")) {
					childNewNodeBatteryArc.getAttributes().getNamedItem("idref").setTextContent(IDNewBatteryTrans);
				}
			}
			nodePageHardware.insertBefore(newNodeBatteryArc, nodesBatteryArc.get(0));
		}
	}
	
	public boolean hasCPNSheet(NodeList nodesCPNSheet, Node childNodeIntanceHardware) {
		String IDChildNodeInstanceHardware = childNodeIntanceHardware.getAttributes().
														getNamedItem("id").getNodeValue() ;
		int m;
		for(m = 0; m < nodesCPNSheet.getLength(); m++) {
			Node noCPNSheet = nodesCPNSheet.item(m);
			if(noCPNSheet.getAttributes().getNamedItem("instance").getNodeValue().
					equals(IDChildNodeInstanceHardware)) {
				return true;
			}
		}
		return false;
	}
}
