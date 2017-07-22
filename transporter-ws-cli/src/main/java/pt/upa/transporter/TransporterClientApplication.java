package pt.upa.transporter;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.upa.transporter.ws.*;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.handler.HeaderClientHandler;

import java.util.Map;

import javax.xml.ws.BindingProvider;


//classes generated from WSDL
//import example.ws.PingFault_Exception;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
public class TransporterClientApplication {
	
	public static final String CLASS_NAME = TransporterClientApplication.class.getSimpleName();
	public static final String TOKEN = "client";
	
	public static void main(String[] args) throws Exception {
		/*// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", TransporterClientApplication.class.getName());
			return;
		}
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...\n");
		
		String uddiURL = args[0];
		System.out.println("uddiURL = " + uddiURL + "\n");
		String name = args[1];
		System.out.println("name = " + name + "\n");
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		//uddiNaming.rebind(name, uddiURL);
		
		
		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);
		System.out.println("EndpointAddress = " + endpointAddress + "\n");
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		TransporterPortType port = service.getTransporterPort();
		
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map requestContext = bindingProvider.getRequestContext();
		String oldEndpointAddress = (String) requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
		//Actual logic here
		try {
			String result = port.ping("friend");
			System.out.println(result);

		} catch (Exception pfe) {
			System.out.println("Caught: " + pfe);
		}*/
		
		TransporterClient c = new TransporterClient(args); 
		c.ping("I AM AN OBJECT :)");
	}
}
