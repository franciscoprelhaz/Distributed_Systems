package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.upa.transporter.ws.handler.HeaderHandler;

@WebService(
		//ALL OF THESE NEED TO BE CHECKED, THEY ARE MORE THAN LIKELY WRONG
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name= "UpaTransporter",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
)
@HandlerChain(file="/handler-chain.xml")


public class TransporterPort implements TransporterPortType{
	public List<JobView> jobs = new ArrayList<JobView>();
	public String[] northernRegions = {"Porto","Braga","Viana do Castelo","Vila Real","Bragança"};
	public String[] centerRegions = {"Lisboa","Leiria","Santarém","Castelo Branco", "Coimbra","Aveiro","Viseu","Guarda"};
	public String[] southernRegions = {"Setúbal", "Évora", "Portalegre", "Beja", "Faro"};
	public ArrayList<String[]> regionsWeOperate = new ArrayList<String[]>();
	public ArrayList<String[]> knownRegions = new ArrayList<String[]>();
	private Random r = new Random();
	private int max = 100;
	private int min = 10;
	public int myi;
	private Integer priceIncrease;
	private Integer lowPriceDecrease;
	private Integer jobCounter = 0;
	public String companyName;
	public Timer timer;
	//soap handler variables
	public static final String CLASS_NAME = TransporterPort.class.getSimpleName();
	public static final String TOKEN = "server";
	@Resource
	private WebServiceContext webServiceContext;
	
	public TransporterPort(int i) {
		super();
		myi = i;
		Integer tempi = myi;
		priceIncrease = r.nextInt(max - min) + min;
		lowPriceDecrease = r.nextInt(min);
		companyName = "UpaTransporter" + tempi.toString();
		knownRegions.add(northernRegions);
		knownRegions.add(centerRegions);
		knownRegions.add(southernRegions);
		if((i%2)== 0) {
			regionsWeOperate.add(northernRegions);
			regionsWeOperate.add(centerRegions);
		}
		else {
			regionsWeOperate.add(southernRegions);
			regionsWeOperate.add(centerRegions);
		}
		
			
	}
	@Override
	public String ping(String name) {
		//We should try to get this working first
		System.out.println("Here's the name I was asked " + name + "\n");
		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext.get(HeaderHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);

		// server processing
		String result = String.format("Hello %s!", name);
		System.out.printf("Result: %s%n", result);

		// *** #7 ***
		// put token in message context
		String newValue = propertyValue + "," + TOKEN;
		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
		messageContext.put(HeaderHandler.RESPONSE_PROPERTY, newValue);

		return result;
		
		//return "Pinging " + name + "!";
	}
	
	/*public String sayHello(String name) {
		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext.get(HeaderHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);

		// server processing
		String result = String.format("Hello %s!", name);
		System.out.printf("Result: %s%n", result);

		// *** #7 ***
		// put token in message context
		String newValue = propertyValue + "," + TOKEN;
		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
		messageContext.put(HeaderHandler.RESPONSE_PROPERTY, newValue);

		return result;
	}*/

	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		//incOrDec = true if price is to be increased, false otherwise
		boolean incOrDec;
		int newPrice;
		if(price < 0 ) {
			throw new BadPriceFault_Exception("Requested job with negative price in transporter", new BadPriceFault());
		}
		boolean foundLocationOrigin = false;
		outerLoopOrigin:
		for(String[] s : knownRegions) {
			for(String so : s) {
				if(so.equals(origin)) {
					foundLocationOrigin = true;
					break outerLoopOrigin;
				}
			}
		}
		boolean foundLocationDestination = false;
		outerLoopDestination:
		for(String[] s : knownRegions) {
			for(String so : s) {
				if(so.equals(destination)) {
					foundLocationDestination = true;
					break outerLoopDestination;
				}
			}
		}
		if(!foundLocationOrigin || !foundLocationDestination)
			throw new BadLocationFault_Exception("Requested job with unknown origin or destination in transporter", new BadLocationFault());
		//Now to search if we operate there...
		foundLocationOrigin = false;
		outerLoopOrigin:
		for(String[] s : regionsWeOperate) {
			for(String so : s) {
				if(so.equals(origin)) {
					foundLocationOrigin = true;
					break outerLoopOrigin;
				}
			}
		}
		foundLocationDestination = false;
		outerLoopDestination:
		for(String[] s : regionsWeOperate) {
			for(String so : s) {
				if(so.equals(destination)) {
					foundLocationDestination = true;
					break outerLoopDestination;
				}
			}
		}
		
		if(!foundLocationOrigin || !foundLocationDestination) {
			//We don't operate here!
			return null;
		}
		if(price > 100) {
			return null;
		}
		JobView j = new JobView();
		if(price <= 10) {
			incOrDec = false;
			newPrice = price - lowPriceDecrease;
		}
		//Price is more than 10 but smaller than 100, the usual case
		else {
			if((myi%2)!= 0 ) {
				//Even number with an uneven transporter
				if((price%2)==0){
					incOrDec = true;
					newPrice = price + priceIncrease;
				}
				//Unever number with an uneven transporter
				else {
					incOrDec = false;
					newPrice = price - priceIncrease;
				}
			}
			//I am an even transporter
			else {
				//Even number with an even transporter
				if((price%2)==0) {
					incOrDec = false;
					newPrice = price - priceIncrease;
				}
				//Unever number with an even transporter
				else {
					incOrDec = true;
					newPrice = price + priceIncrease;
				}
			}
		}
		//Certify that price is not negative, and then set it.
		newPrice = (newPrice < 0 ? -newPrice : newPrice);
		if((!incOrDec) && (newPrice >= price)) {
			/*Due to the conversions from negative to positive, we ended up with a larger price when we
			wanted a smaller one, careful arithmetic required!
			Example: 2 - 8 = -6 => 6
			Solution: - (2 - 6) = 4 => - (2 - 4) = 2 => - (2 - 2) = 0; done
			Example: 3 - 8 = -5 => 5
			solution: -(3 - 5) = 2; done
			
			*/
			while(newPrice >= price)
				newPrice = -(price - newPrice);
			
		}
		j.setJobPrice(newPrice);

		j.setCompanyName(companyName);
		jobCounter = jobCounter + 1;
		j.setJobIdentifier(jobCounter.toString());
		j.setJobOrigin(origin);
		j.setJobDestination(destination);
		//Price is set above
		j.setJobState(JobStateView.PROPOSED);
		/*"companyName",
	    "jobIdentifier",
	    "jobOrigin",
	    "jobDestination",
	    "jobPrice",
	    "jobState"*/
		jobs.add(j);
		return j;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		for(JobView j : jobs) {
			if(id.equals(j.getJobIdentifier())) {
				if(accept){
					j.setJobState(JobStateView.ACCEPTED);
					timer = new Timer();
					timer.schedule(new TransporterSimulator(j), 5000);
				
				}else{
					j.setJobState(JobStateView.REJECTED);}
				return j;
			}
		}
		throw new BadJobFault_Exception("ID for decide job was not found",new BadJobFault());
	}

	@Override
	public JobView jobStatus(String id) {
		//NOT SURE IF IT'S DONE LIKE THIS
		for(JobView j : jobs) {
			if(id.equals(j.getJobIdentifier())) {
				return j;
			}
		}
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		return jobs;
	}

	@Override
	public void clearJobs() {
		jobs.clear();
	}

}
