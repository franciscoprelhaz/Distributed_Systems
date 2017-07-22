package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import javax.jws.WebService;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.broker.ws.cli.BrokerClient;
@WebService(
		//ALL OF THESE NEED TO BE CHECKED, THEY ARE MORE THAN LIKELY WRONG
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name= "UpaBroker",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType{
	private String[] northernRegions = {"Porto","Braga","Viana do Castelo","Vila Real","Bragança"};
	private String[] centerRegions = {"Lisboa","Leiria","Santarém","Castelo Branco", "Coimbra","Aveiro","Viseu","Guarda"};
	private String[] southernRegions = {"Setúbal", "Évora", "Portalegre", "Beja", "Faro"};
	BrokerClient slave = null;
	TransporterClient transportadora; // falta fazer transportadora = new TransporterClient(argumentos manhosos)
	List<TransporterClient> transporters = new ArrayList<TransporterClient>();
	List<TransportView> transports = new ArrayList<TransportView>();
	List<TransportView> transportsBackup = new ArrayList<TransportView>();
	List<JobView> acceptedJobs = new ArrayList<JobView>();
	Integer counter = 0;
	Integer counterBackup = 0;
	public Timer timer;
	/*public void addTransporter(String[] t){
		try {
			transporters.add(new TransporterClient(t));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void setSlave(BrokerClient bc) {
		slave = bc;
	}
	@Override
	public void updateSlave(TransportView tv, Integer count) {
		if(count != -1 && slave!=null) {
			slave.updateSelf(tv,count);
			return;
		}
		if(count == -1 && slave ==null) {
			this.becomeTheMaster();
			return;
		}
		transportsBackup.add(tv);
		counterBackup = count;
	}
	
	public void becomeTheMaster() {
		transports = transportsBackup;
		counter = counterBackup;
		//FIX THE NAMES
	}
	
	public void setTransporters(List<TransporterClient> l){
		transporters = l;
	}
	
	public TransportView jobToTransport(JobView j) {
		TransportView tempT = new TransportView();
		tempT.setId(j.getJobIdentifier());
		tempT.setOrigin(j.getJobOrigin());
		tempT.setDestination(j.getJobDestination());
		tempT.setPrice(j.getJobPrice());
		tempT.setTransporterCompany(j.getCompanyName());
		//SETTING THE JOB IS VERY DANGEROUS
		//tempT.setState(TransportStateView.fromValue(j.getJobState().value()));
		return tempT;
	}
	@Override
	public String ping(String name) {
		// TODO Auto-generated method stub
		System.out.println("Here's the name I was asked " + name + "\n");
		return "Pinging " + name + "!";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		if(price < 0){
			throw new InvalidPriceFault_Exception("preco pedido é negativo", new InvalidPriceFault());
		}
		if(!Arrays.asList(northernRegions).contains(origin) && !Arrays.asList(centerRegions).contains(origin) && !Arrays.asList(southernRegions).contains(origin)){
			throw new UnknownLocationFault_Exception("origem desconhecida", new UnknownLocationFault());
		}
		if(!Arrays.asList(northernRegions).contains(destination) && !Arrays.asList(centerRegions).contains(destination) && !Arrays.asList(southernRegions).contains(destination)){
			throw new UnknownLocationFault_Exception("destino desconhecido", new UnknownLocationFault());
		}
		
		TransportView t = new TransportView();
		t.setOrigin(origin);
		t.setDestination(destination);
		//t.setId(counter.toString());
		//Price will be set once again later;
		t.setPrice(price);
		t.setTransporterCompany("Nobody");
		t.setState(TransportStateView.REQUESTED);
		transports.add(t);
		
		//List<TransportView> tempTransList = new ArrayList<TransportView>();
		List<JobView> jobs = new ArrayList<JobView>();
		try {
			for (TransporterClient tc : transporters) {
				JobView tempJob = tc.requestJob(origin, destination, price);
				if(tempJob != null)
					jobs.add(tempJob);	
			}
		} 
		catch (BadLocationFault_Exception e) {
			e.printStackTrace();
		} 
		catch (BadPriceFault_Exception e) {
			e.printStackTrace();
		}
		if(jobs.isEmpty()) { 
			//fix me
			t.setOrigin(origin);
			t.setDestination(destination);
			t.setPrice(price);
			t.setTransporterCompany("Nobody");
			t.setState(TransportStateView.FAILED);
			throw new UnavailableTransportFault_Exception("Couldn't find a transport for a particular job", new UnavailableTransportFault()); 
		}
		boolean Overpriced = true;
		for(JobView j : jobs) {
			if(j.getJobPrice() < price) {
				Overpriced = false;
				break;
			}
			else {
				System.out.println("I gave the ludicrous price offer of" + j.getJobPrice());
			}
		}
		
		t.setState(TransportStateView.BUDGETED);
		
		if(Overpriced) {
			for(JobView j : jobs) {
				j.setJobState(JobStateView.REJECTED);
			}
			t.setState(TransportStateView.FAILED);
			throw new UnavailableTransportPriceFault_Exception("All the transport offers I got where overpriced!",new UnavailableTransportPriceFault());
		}
		String idLowest = "This should never happen, I am an ID for a job in broker port";
		int lowestPrice = 1000;
		
		for(JobView j : jobs) {
			if(j.getJobPrice() < lowestPrice) {
				idLowest = j.getJobIdentifier();
				lowestPrice = j.getJobPrice();
			}
		}
		for(JobView j : jobs) {
			if(j.getJobIdentifier() == idLowest) {
				t.setState(TransportStateView.BOOKED);
				t.setId(j.getJobIdentifier());
				updateSlave(t,counter);
				j.setJobState(JobStateView.ACCEPTED);
				t.setTransporterCompany(j.getCompanyName());
				//This is a horrible hack
				for(TransporterClient tc : transporters) {
					//tc.startStateTime(j);
					acceptedJobs.add(j);
					break;
				}
			}
			else {
				j.setJobState(JobStateView.REJECTED);
			}
		}
		
		return idLowest;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception { 
		//transportes_todos = listTransports();
		System.out.println("Here's the ID of the transport I was asked to view: " + id);
		for(TransportView aux : transports){
			if(aux.getId().equals(id) ){
				/*for(JobView j : acceptedJobs) {
					if(j.getJobIdentifier().equals(id)) {
						
					}
				}*/
				return aux;
			}
		}

		//I'll only get here if there's no such transport
		throw new UnknownTransportFault_Exception("I was asked to report on a transport that does not exist",new UnknownTransportFault());
	}

	@Override
	public List<TransportView> listTransports() {
		return transports;
	}

	@Override
	public void clearTransports() {
		for (TransporterClient trans :transporters){
			trans.clearJobs();
		}
		
	}
}