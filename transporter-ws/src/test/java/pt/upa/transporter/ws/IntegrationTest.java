package pt.upa.transporter.ws;

import org.junit.*;

import mockit.Expectations;
import mockit.Mocked;

import static org.junit.Assert.*;

import javax.xml.ws.BindingProvider;

/**
 *  Unit Test example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class IntegrationTest {

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
    public void testNameAndId() {
    	TransporterPort tp = new TransporterPort(5);
    	assertEquals("UpaTransporter5",tp.companyName);
        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testBadLocations() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	tp.requestJob("Nonsense", "More Nonsense", 10);
    }
    @Test
    public void testGoodLocations() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	JobView j = tp.requestJob("Lisboa", "Santarém", 20);
    	assertEquals("Lisboa",j.getJobOrigin());
    	//assertEquals("UpaTransporter5",tp.companyName);
        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    @Test(expected = BadPriceFault_Exception.class)
    public void testNegativePrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	JobView j = tp.requestJob("Lisboa", "Santarém", -10);
    	//assertEquals("UpaTransporter5",tp.companyName);
        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
    @Test(expected = BadLocationFault_Exception.class)
    public void testNullLocations() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	tp.requestJob(null,null,10);
    }
    @Test
    public void testLowerPrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	JobView j = tp.requestJob("Lisboa", "Santarém",2);
    	//System.out.println("Price for the job " +  j.getJobPrice());
    	assertEquals(true,(2>j.getJobPrice()));
    }
    @Test
    public void testNormalPriceIncrease() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	TransporterPort tp = new TransporterPort(5);
    	JobView j = tp.requestJob("Lisboa", "Santarém",20);
    	//System.out.println("Price for the job " +  j.getJobPrice());
    	assertEquals(true,(20<j.getJobPrice()));
    }
    /*@Test
    public <P extends TransporterPortType & BindingProvider> void testMockServerException(
        @Mocked final TransporterService service,
        @Mocked final P port)
        throws Exception {

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new Expectations() {{
            new TransporterService();
            service.getTransporterPort(); //.getCalcPort(); result = port;
            result = port;
            port.ping("friend");
            result = new WebServiceException("fabricated");
        }};


        // Unit under test is exercised.
        CalcClient client = new CalcClient();
        // call to mocked server
        client.sum(1,2);
    }*/

}