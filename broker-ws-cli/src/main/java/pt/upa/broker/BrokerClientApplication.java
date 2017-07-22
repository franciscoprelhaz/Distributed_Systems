package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.upa.broker.ws.*;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.broker.ws.cli.BrokerClientFrontEnd;
import pt.upa.transporter.ws.cli.TransporterClient;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

//classes generated from WSDL
//import example.ws.PingFault_Exception;



import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
public class BrokerClientApplication {
	static BrokerClientFrontEnd c;
	//private static IamAlive alive = new IamAlive();
	public static void main(String[] args) throws Exception {
		for(String s : args) {
			System.out.println("Here's the args " + s + "\n\n");
		}
		c = new BrokerClientFrontEnd("9");
		System.out.println(c.ping("friend"));
		// REQUEST TRANSPORT NAO FUNCIONA
		//alive.setBca(c);
		
			try {
				String idMyJob = c.requestTransport("Lisboa", "Castelo Branco", 5);
				c.viewTransport(idMyJob);
				idMyJob = c.requestTransport("Beja", "Lisboa", 5);
				c.viewTransport(idMyJob);
			} catch (Exception e) {
				System.out.println("Something went wrong when requesting a single transport");
			}
			try {
				String idMyJob2 = c.requestTransport("Beja", "Faro", 50);
				if(!(idMyJob2.equals("No transporter gave me a good offer, try again and perhaps we'll get lucky...")))
					c.viewTransport(idMyJob2);
			} catch (Exception e) {
				System.out.println("Something went wrong when requesting transports");
			}
			System.out.println("Going to list all the transports I've requested:");
			int i = 0;
		checkForMaster:
		while(true) {
			//alive.run();
			try {
				System.out.println("The master is alive for the " + i + "cycle, listing transporters");
				i++;
				c.listTransports();
			}
			catch(WebServiceException e) {
				System.out.println("THE MASTER HAS DIED A HORRIBLE DEATH");
				break checkForMaster;
			}
			//c.clearTransports();
			//System.out.println("SWITCHING FROM MASTER TO SLAVE\n\n\n\n");
		}
			c.switchBrokers();
			c.listTransports();
			c.clearTransports();

	}

	public String ping(String string) {
		return c.ping(string);
	}
}
