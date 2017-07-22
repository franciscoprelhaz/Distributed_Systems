package pt.upa.broker.ws.cli;

import java.util.List;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClientFrontEnd {
	BrokerClient master;
	BrokerClient slave;
	/*Default name for the primary broker. 
	One COULD have a different master, but the expected execution implies
	that one started the application with the brokerclient's -Dws.slave=<some int> option
	*/
	String masterName = "UpaBroker0";
	//The slave name is by definition, it COULD change.
	String slaveName = "";
	String[] argsforClient = {"http://localhost:9090", masterName};
	boolean masterAlive = true;
	public BrokerClientFrontEnd(String SN) {
		slaveName = "UpaBroker9";
		try {
			master = new BrokerClient(argsforClient);
		} 
		catch (Exception e) {
			System.out.println("Something went horribly wrong in the client front end!");
			e.printStackTrace();
		}
		try {
			String[] argsforSlave = {"http://localhost:9090", slaveName};
			slave = new BrokerClient(argsforSlave);
		} 
		catch (Exception e) {
			System.out.println("Something went horribly wrong in the client front end with the slave!!");
			e.printStackTrace();
		}
	}	
	public void isMasterAlive() {
		while(masterAlive) {
			try {
				Thread.sleep(100);
				String result = master.ping("I am alive");
			} catch (InterruptedException e) {
				// The master has died!
				switchBrokers();
			}
		}
	}
	
	public void switchBrokers() {
		master = slave;
		master.becomeTheMaster();
	}
	
	public String ping(String name){
		return master.ping(name);
	}
	
	public String requestTransport(String origin, String destination, int price) 
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, 
				UnknownLocationFault_Exception {
		return master.requestTransport(origin, destination, price);
	}
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return master.viewTransport(id);
	}
	public TransportView viewTransport(TransportView t) throws UnknownTransportFault_Exception {
		return master.viewTransport(t);
	}
	
	public List<TransportView> listTransports() {
		return master.listTransports();
	}
	
	//This HAS to be used with care!
	public void clearTransports() {
		master.clearTransports();
	}
	
	//Setters and getters only bellow.
	public BrokerClient getBc() {
		return master;
	}
	public void setBc(BrokerClient bc) {
		this.master = bc;
	}
	public String getMasterName() {
		return masterName;
	}
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	public String getSlaveName() {
		return slaveName;
	}
	public void setSlaveName(String slaveName) {
		this.slaveName = slaveName;
	}
	public String[] getArgsforClient() {
		return argsforClient;
	}
	public void setArgsforClient(String[] argsforClient) {
		this.argsforClient = argsforClient;
	}
	
}
