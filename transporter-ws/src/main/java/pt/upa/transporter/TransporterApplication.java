package pt.upa.transporter;

import javax.xml.ws.Endpoint;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.TransporterPort;
import java.util.Timer;
import java.util.TimerTask;

public class TransporterApplication {
	public Timer timer;
	
	
	public static void main(String[] args) throws Exception {
			System.out.println(TransporterApplication.class.getSimpleName() + " starting...");
			//Here there be monsters
			// Check arguments
	        if (args.length < 1) {
	            System.err.println("Argument(s) missing in transporter-ws!");
	            System.err.printf("Usage: java %s url%n", TransporterApplication.class.getName());
	            return;
	        }
	        
	        //String url = args[0];
	        String uddiURL = args[0];
	        System.out.println("uddiURL = " + uddiURL + "\n");
			String name = args[1];
			System.out.println("name = " + name + "\n");
			String url = args[2];
			System.out.printf("url = "  + url + "\n");
			System.out.println("I am the transporter number " + args[3]);
	        Endpoint endpoint = null;
	        UDDINaming uddiNaming = null;
	        try {
	            endpoint = Endpoint.create(new TransporterPort(Integer.parseInt(args[3])));
	
	            // publish endpoint
	            System.out.printf("Starting %s%n", url);
	            endpoint.publish(url);
	            
	         // publish to UDDI
				System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(name, url);
				System.out.printf("Publishing " + uddiNaming + "\n");
	            
	            // wait
	            System.out.println("Awaiting connections");
	            System.out.println("Press enter to shutdown");
	            System.in.read();
	
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
}
