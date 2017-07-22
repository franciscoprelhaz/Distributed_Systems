package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import pt.upa.broker.ws.BrokerPort;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.transporter.TransporterClientApplication;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.handler.HeaderClientHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
public class BrokerApplication {
	
	public static final String CLASS_NAME = TransporterClientApplication.class.getSimpleName();
	public static final String TOKEN = "client";
	
	public static void main(String[] args) throws Exception {
			System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
			//Here there be monsters
			// Check arguments
	        if (args.length < 2) {
	            System.err.println("Argument(s) missing! in brokerapplication");
	            System.err.printf("Usage: java %s url%n", BrokerApplication.class.getName());
	            return;
	        }
	        
	        /*if (args.length < 1) {
				System.out.println("Argument(s) missing!");
				System.out.printf("Usage: java %s url%n", TransporterClientApplication.class.getName());
				return;
			}*/

			/*String url = args[0];

			// create stub
			TransporterService service = new TransporterService();
			TransporterPortType port = service.getTransporterPort();

			// access request context
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();

			// *** #1 ***
			// put token in request context
			String initialValue = TOKEN;
			System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
			requestContext.put(HeaderClientHandler.REQUEST_PROPERTY, initialValue);

			// set endpoint address
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			// make remote call
			System.out.printf("Remote call to %s ...%n", url);
			String result = port.ping("friend");
			System.out.printf("Result: %s%n", result);

			// access response context
			Map<String, Object> responseContext = bindingProvider.getResponseContext();

			// *** #12 ***
			// get token from response context
			String finalValue = (String) responseContext.get(HeaderClientHandler.RESPONSE_PROPERTY);
			System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
			*/
	
	        //String url = args[0];
	        String uddiURL = args[0];
	        System.out.println("uddiURL = " + uddiURL + "\n");
			String name = args[1];
			System.out.println("name = " + name + "\n");
			String url = args[2];
			System.out.printf("url = "  + url + "\n");
	        Endpoint endpoint = null;
	        //careful
	        //UDDINaming uddiNaming = null;
	        UDDINaming uddiNaming = new UDDINaming(uddiURL);
	        
	     // create stub
 			
	     			
	        try {
	        	//Before publishing my own URL, I must look for a possible slave!
	        	System.out.println("Looking for a possible slave...");
	        	Collection<String> argsBC;
	            List<BrokerClient> listofBC = new ArrayList<BrokerClient>();
	            try {
	            	argsBC = uddiNaming.list("UpaBroker%");
	            	if(!argsBC.isEmpty()) {
	            		String[] tempARGS = (String[]) argsBC.toArray(new String[0]);
	            		if(!tempARGS[0].equals(url)) {
		            		System.out.println("I think I found a slave! Here's his details:");
		            		for(String s : tempARGS) {
		            			System.out.println(s);
		            		}
		            		listofBC.add(new BrokerClient(tempARGS));
	            		}
	            	}
	            } 
	            catch(Exception e) {
	            	//It seems like there's no slaves available for me!
	            	e.printStackTrace();
	            	System.out.println("I have found no slaves!");
	            }
	        	BrokerPort bp = new BrokerPort();
	        	if(!listofBC.isEmpty()) {
	        		bp.setSlave(listofBC.get(0));
	        	}
	        	//Now that we've finished looking for slaves we can start the actual work
	            endpoint = Endpoint.create(bp);
	
	            // publish endpoint
	            System.out.printf("Starting %s%n", url);
	            endpoint.publish(url);
	            
	         // publish to UDDI
				System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(name, url);
				System.out.printf("Publishing " + uddiNaming + "\n");
	            
				//Initialize the transporters;
	            Collection<String> argsTC;
	            List<TransporterClient> listofTC = new ArrayList<TransporterClient>();
	            argsTC = uddiNaming.list("UpaTransporter%");
	            for(String s : argsTC) {
	            	String[] sl = {s};
	            	System.out.println("Here's my endpoint address" + s + "\n");
	            	listofTC.add(new TransporterClient(sl));
	            }
	            bp.setTransporters(listofTC);
	            
	            TransporterClient transportadora = listofTC.get(0);
	        	//TransporterClient transportadora = new TransporterClient(argsTC);
				
	            // wait
	            System.out.println("Awaiting connections");
	            System.out.println("Press enter to shutdown");
	            transportadora.ping("caramelo");
	            System.in.read();
	            
	            TransporterService service = new TransporterService();
	 			TransporterPortType port = service.getTransporterPort();

	 			// access request context
	 			BindingProvider bindingProvider = (BindingProvider) port;
	 			Map<String, Object> requestContext = bindingProvider.getRequestContext();

	 			// *** #1 ***
	 			// put token in request context
	 			String initialValue = TOKEN;
	 			System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
	 			requestContext.put(HeaderClientHandler.REQUEST_PROPERTY, initialValue);

	 			// set endpoint address
	 			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

	 			// make remote call
	 			System.out.printf("Remote call to %s ...%n", url);
	 			String result = port.ping("friend");
	 			System.out.printf("Result: %s%n", result);

	 			// access response context
	 			Map<String, Object> responseContext = bindingProvider.getResponseContext();

	 			// *** #12 ***
	 			// get token from response context
	 			String finalValue = (String) responseContext.get(HeaderClientHandler.RESPONSE_PROPERTY);
	 			System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
	
	        } catch(Exception e) {
	            System.out.printf("Caught exception: %s%n", e);
	            e.printStackTrace();
	
	        } finally {
	            try {
	                if (endpoint != null) {
	                    // stop endpoint
	                    endpoint.stop();
	                    System.out.printf("Stopped %s%n", url);
	                }
	            } catch(Exception e) {
	                System.out.printf("Caught exception when stopping: %s%n", e);
	            }
	            try {
					if (uddiNaming != null) {
						// delete from UDDI
						uddiNaming.unbind(name);
						System.out.printf("Deleted '%s' from UDDI%n", name);
					}
				} catch (Exception e) {
					System.out.printf("Caught exception when deleting: %s%n", e);
				}
	        }
	        
	        
	
	    }
		//no more monsters
		
	

}
