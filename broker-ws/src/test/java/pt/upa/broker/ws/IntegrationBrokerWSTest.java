package pt.upa.broker.ws;

import org.junit.*;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

import static org.junit.Assert.*;

/**
 *  Unit Test example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class IntegrationBrokerWSTest {

    // static members


    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {

    }

    @AfterClass
    public static void oneTimeTearDown() {

    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    // tests


    @Test
    public void testJobToTransport() {
    	BrokerPort bp = new BrokerPort();
    	boolean transportIsFine = false;
    	//The jobs themselfs are tested in transporter-ws
    	JobView j = new JobView();
    	j.setCompanyName("A");
    	j.setJobIdentifier("1");
    	j.setJobOrigin("Lisboa");
    	j.setJobDestination("Lisboa");
    	j.setJobPrice(12);
    	j.setJobState(JobStateView.COMPLETED);
        TransportView t = bp.jobToTransport(j);
        if(
        	j.getCompanyName().equals(t.getTransporterCompany()) &&
        	j.getJobIdentifier().equals(t.getId()) &&
        	j.getJobOrigin().equals(t.getOrigin()) &&
        	j.getJobDestination().equals(t.getDestination()) &&
        	j.getJobPrice() == t.getPrice()
    	) {
        	transportIsFine = true;
        }
        assertEquals(true,transportIsFine);
    }
    //Requesting a transport when there are no transporters up
    @Test(expected = UnavailableTransportFault_Exception.class)
    public void testBadTransporters() throws UnknownLocationFault_Exception,  InvalidPriceFault_Exception,
    	UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception {
    	BrokerPort bp = new BrokerPort();
    	bp.requestTransport("Lisboa", "Lisboa", 15);
    }
    @Test(expected = UnknownLocationFault_Exception.class)
    public void testBadDestination() throws UnknownLocationFault_Exception,  InvalidPriceFault_Exception,
    	UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception {
    	BrokerPort bp = new BrokerPort();
    	bp.requestTransport("Lisboa", "nonsense", 15);
    }
    @Test(expected = UnknownLocationFault_Exception.class)
    public void testBadOrigin() throws UnknownLocationFault_Exception,  InvalidPriceFault_Exception,
    	UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception {
    	BrokerPort bp = new BrokerPort();
    	bp.requestTransport("nonsense", "Lisboa", 15);
    }
    @Test(expected = UnknownLocationFault_Exception.class)
    public void testnullLocation() throws UnknownLocationFault_Exception,  InvalidPriceFault_Exception,
    	UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception {
    	BrokerPort bp = new BrokerPort();
    	bp.requestTransport(null,null, 15);
    }
    @Test(expected = InvalidPriceFault_Exception.class)
    public void testBadPrice() throws UnknownLocationFault_Exception,  InvalidPriceFault_Exception,
    	UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception {
    	BrokerPort bp = new BrokerPort();
    	bp.requestTransport("Lisboa","Lisboa", -15);
    }
}