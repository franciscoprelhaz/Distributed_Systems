package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.TransporterClientApplication;
import pt.upa.transporter.ws.BadJobFault;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class TransporterClient {
	String URL;
	String name;
	UDDINaming uddiNaming;
	String endpointAddress;
	TransporterService service;
	TransporterPortType port;
	BindingProvider bindingProvider;
	Map requestContext;
	String oldEndpointAddress;
	public Timer timer;
	public TransporterClient(String[] args) throws Exception{
		// Check arguments
		if (args.length < 1) {
			System.err.println("Argument in transporter client is missing!");
			System.err.printf("Usage: java %s uddiURL name%n", TransporterClientApplication.class.getName());
			return;
		}
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...\n");

		endpointAddress = args[0];//[0];
		System.out.println("URL = " + endpointAddress + "\n");
		/*name = args[1];
		System.out.println("name = " + name + "\n");
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		uddiNaming = new UDDINaming(uddiURL);*/
		// uddiNaming.rebind(name, uddiURL);

		//System.out.printf("Looking for '%s'%n", name);
		//endpointAddress = uddiNaming.lookup(name);
		System.out.println("EndpointAddress = " + endpointAddress + "\n");
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		System.out.println("Creating stub ...");
		service = new TransporterService();
		port = service.getTransporterPort();

		System.out.println("Setting endpoint address ...");
		bindingProvider = (BindingProvider) port;
		requestContext = bindingProvider.getRequestContext();
		oldEndpointAddress = (String) requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		// Actual logic here
		try {
			String result = port.ping("friend");
			System.out.println(result);

		} catch (Exception pfe) {
			System.out.println("Caught: " + pfe);
		}
	}
	
	public String ping(String name) {
		return "I am a T-cli and I am going to ping mah friend" + port.ping(name);
	}
	
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		return port.requestJob(origin, destination, price);
	}

	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return port.decideJob(id, accept);
	}


	public JobView jobStatus(String id) {
		return port.jobStatus(id);
	}

	public List<JobView> listJobs() {
		return port.listJobs();
	}
	
	public void clearJobs() {
		port.clearJobs();
	}
	
	public void startStateTime(JobView j) {
		timer = new Timer();
		timer.schedule(new TransporterSimulator(j), 5000);
	}
}
