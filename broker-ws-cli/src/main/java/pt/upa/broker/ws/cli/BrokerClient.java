package pt.upa.broker.ws.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.xml.ws.BindingProvider;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.BrokerClientApplication;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient {

	// TODO
	List<TransportView> transportsBackup = new ArrayList<TransportView>();
	//We probably don't NEED the counter backup, but it doesn't hurt!
	Integer counterBackup;
	BrokerPortType port = null;
	BrokerService service;
	BindingProvider bindingProvider;
	Map requestContext;
	String endpointAddress;// = uddiNaming.lookup(name);
	public BrokerClient(String[] args) throws Exception{
		// Check arguments
				if(args.length == 1) {
					endpointAddress = args[0];
				}
				else {
					//System.out.println("I have less than two arguments, If I'm not connecting to a slave this is an error");
					System.err.printf("Usage: java %s uddiURL name%n", BrokerClientApplication.class.getName());
					//return;
				//}
					System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...\n");
					
					String uddiURL = args[0];
					System.out.println("uddiURL = " + uddiURL + "\n");
					String name = args[1];
					System.out.println("name = " + name + "\n");
					System.out.printf("Contacting UDDI at %s%n", uddiURL);
					UDDINaming uddiNaming = new UDDINaming(uddiURL);
					//uddiNaming.rebind(name, uddiURL);
					
					
					System.out.printf("Looking for '%s'%n", name);
					endpointAddress = uddiNaming.lookup(name);
					System.out.println("EndpointAddress = " + endpointAddress + "\n");
					if (endpointAddress == null) {
						System.out.println("Not found!");
						return;
					} else {
						System.out.printf("Found %s%n", endpointAddress);
					}
				}
					
				System.out.println("Creating stub ...");
				service = new BrokerService();
				port = service.getBrokerPort();
				
				System.out.println("Setting endpoint address ...");
				bindingProvider = (BindingProvider) port;
				requestContext = bindingProvider.getRequestContext();
				String oldEndpointAddress = (String) requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
				
				/*
				//Actual logic here
				try {
					String result = port.ping("friend");
					System.out.println(result);

				} catch (Exception pfe) {
					System.out.println("Caught: " + pfe);
				}*/
	}
	
	public String ping(String name){
		return "I am a client and I'm going going to ping a broker " + port.ping(name);
	}
	
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnknownLocationFault_Exception {
		try {
			return port.requestTransport(origin, destination, price);
		}
		catch(UnavailableTransportPriceFault_Exception e) {
			return "No transporter gave me a good offer, try again and perhaps we'll get lucky...";
		}
	}
	
	public void updateSelf(TransportView tv,Integer count) {
		port.updateSlave(tv, count);
	}
	
	public void becomeTheMaster() {
		port.updateSlave(null,-1);
	}
	
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		
		TransportView transporte= port.viewTransport(id);
		System.out.println("Listing transport: \n");
		System.out.println("ID: " + transporte.getId());
		System.out.println("Origem: " + transporte.getOrigin());
		System.out.println("Destino: " + transporte.getDestination());
		System.out.println("Companhia: " + transporte.getTransporterCompany());
		System.out.println("Preco: " + transporte.getPrice().toString());
		System.out.println("Estado: " + transporte.getState().value());
		System.out.println("Listing transport: \n");
		System.out.println("Finished listing transport \n");
		
		return transporte;
	}
	public TransportView viewTransport(TransportView t) throws UnknownTransportFault_Exception {
		
		TransportView transporte= t;
		System.out.println("Listing transport: \n");
		System.out.println("ID: " + transporte.getId());
		System.out.println("Origem: " + transporte.getOrigin());
		System.out.println("Destino: " + transporte.getDestination());
		System.out.println("Companhia: " + transporte.getTransporterCompany());
		System.out.println("Preco: " + transporte.getPrice().toString());
		System.out.println("Estado: " + transporte.getState().value());
		System.out.println("Listing transport: \n");
		System.out.println("Finished listing transport \n");
		
		return transporte;
	}
	
	public List<TransportView> listTransports() {
		List<TransportView> lt =  port.listTransports();
		try {
			for(TransportView t : lt) {
				viewTransport(t);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return lt;
	}
	
	public void clearTransports() {
		port.clearTransports();
	}

}
