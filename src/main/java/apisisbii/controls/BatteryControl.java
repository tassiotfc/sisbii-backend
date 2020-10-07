package apisisbii.controls;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisisbii.models.DAO.BatteryDAO;
import apisisbii.models.DAO.InsulinPumpDetailedDAO;

@RestController
@RequestMapping(value="battery")
public class BatteryControl {
	
	public final String ID_PAG_HARDWARE = "ID1436919045";
	public final String ID_INST_TRANS_HARDWARE = "ID1436919090";
	public final String ID_OKB1 = "ID1501681046";
	public final String ID_OKB2 = "ID1502342128";
	public final String ID_OKB3 = "ID1437162252";
	
	@GetMapping("/quantity")
	public int getQuantityBattery() throws Exception {
		InsulinPumpDetailedDAO insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
		Document doc = insulinPumpDetailedDAO.getDocumentInsulinPump();
				
		Element nodePageHardware = doc.getElementById(ID_PAG_HARDWARE);
		NodeList nodesTrans = nodePageHardware.getElementsByTagName("trans");
		
		BatteryDAO batteryDAO = new BatteryDAO();
		int numberBatteryInstance =  batteryDAO.countBatteryIntance(nodesTrans);
		return numberBatteryInstance;
	}
	
	@PostMapping("/add")
	public boolean addBatteryInstance(@RequestBody int number) throws Exception{
		InsulinPumpDetailedDAO insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
		Document doc = insulinPumpDetailedDAO.getDocumentInsulinPump();
				
		Element nodePageHardware = doc.getElementById(ID_PAG_HARDWARE);
		NodeList nodesTrans = nodePageHardware.getElementsByTagName("trans");
		
		BatteryDAO batteryDAO = new BatteryDAO();
		int numberBatteryInstance =  batteryDAO.countBatteryIntance(nodesTrans);
		if(numberBatteryInstance >= 1) {
			batteryDAO.setBatteryInstanceQuantity(doc, ID_OKB1, ID_OKB2, ID_OKB3,
					(numberBatteryInstance + number));
			
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
				newChildNodeInstanceHardware.getAttributes().getNamedItem("id").
					setTextContent(batteryDAO.generateNewID());
				newChildNodeInstanceHardware.getAttributes().getNamedItem("trans").
					setTextContent(IDNewBatteryTrans);
				nodeInstanceHardware.insertBefore(newChildNodeInstanceHardware, childNodeInstanceHardware);
				
				insulinPumpDetailedDAO.saveInsulinPump(doc);
				
				numberBatteryInstance++;
			}
			return true;
		}
		return false;
	}
	
	@PostMapping("/rm")
	public boolean removeBatteryInstance(@RequestBody int number) throws Exception{
		InsulinPumpDetailedDAO insulinPumpDetailedDAO = new InsulinPumpDetailedDAO();
		Document doc = insulinPumpDetailedDAO.getDocumentInsulinPump();
		
		Element nodePageHardware = doc.getElementById(ID_PAG_HARDWARE);
		NodeList nodesTrans = nodePageHardware.getElementsByTagName("trans");
		
		BatteryDAO batteryDAO = new BatteryDAO();
		int numberBatteryInstance =  batteryDAO.countBatteryIntance(nodesTrans);
		if(numberBatteryInstance > number) {
			batteryDAO.setBatteryInstanceQuantity(doc, ID_OKB1, ID_OKB2, ID_OKB3,
					(numberBatteryInstance - number));
			
			int k;
			Element nodeInstanceHardware = doc.getElementById(ID_INST_TRANS_HARDWARE);
			NodeList nodesCPNSheet = doc.getElementsByTagName("cpnsheet");
			for(k  = 1; k <= number; ) {
				Node nodeBatteryTrans = batteryDAO.getNodeBatteryTrans(nodesTrans);
				String IDBattery = nodeBatteryTrans.getAttributes().getNamedItem("id").getNodeValue();
				
				int l;
				NodeList childsNodeInstanceHardware = nodeInstanceHardware.getChildNodes();
				for(l = 0; l < childsNodeInstanceHardware.getLength(); l++) {
					Node childNodeIntanceHardware = childsNodeInstanceHardware.item(l);
					if(childNodeIntanceHardware.hasAttributes()) {
						if(childNodeIntanceHardware.getAttributes().getNamedItem("trans").
								getNodeValue().equals(IDBattery)) {
							if(!batteryDAO.hasCPNSheet(nodesCPNSheet, childNodeIntanceHardware)) {
								nodeInstanceHardware.removeChild(childNodeIntanceHardware);
								NodeList nodesArc = nodePageHardware.getElementsByTagName("arc");
								List<Node> nodesBatteryArc = batteryDAO.getNodesBatteryArcs(nodesArc, IDBattery);
								for (Node node : nodesBatteryArc) {
									nodePageHardware.removeChild(node);
								}
								nodePageHardware.removeChild(nodeBatteryTrans);
								k++;
							}
						}
					}
				}
			}
			
			insulinPumpDetailedDAO.saveInsulinPump(doc);
			return true;
		}
		return false;
	}
}
