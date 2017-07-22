package pt.upa.transporter.ws.handler;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String CONTEXT_PROPERTY = "my.property";
    public static final String REQUEST_PROPERTY = "my.request.property";
	public static final String RESPONSE_PROPERTY = "my.response.property";

	public static final String REQUEST_HEADER = "myRequestHeader";
	public static final String REQUEST_NS = "urn:example";

	public static final String RESPONSE_HEADER = "myResponseHeader";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = HeaderHandler.class.getSimpleName();
	public static final String TOKEN = "server-handler";
    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("AddHeaderHandler: Handling message.");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        /*try {
            if (outboundElement.booleanValue()) {
                System.out.println("Writing header in outbound SOAP message...");

                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName("myHeader", "d", "http://demo");
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                int value = 22;
                String valueString = Integer.toString(value);
                element.addTextNode(valueString);

            } else {
                System.out.println("Reading header in inbound SOAP message...");

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                // get first header element
                Name name = se.createName("myHeader", "d", "http://demo");
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String valueString = element.getValue();
                int value = Integer.parseInt(valueString);

                // print received header
                System.out.println("Header value is " + value);

                // put header in a property context
                smc.put(CONTEXT_PROPERTY, value);
                // set property scope to application client/server class can access it
                smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);

            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }

        return true;*/
        
        
        if(outboundElement){
        	String propertyValue = (String) smc.get(RESPONSE_PROPERTY);
			System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);
			
			try{
				//get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				
				//add header
				SOAPHeader sh = se.getHeader();
				if(sh == null){
					sh = se.addHeader();
				}
				
				//add header element(name, namespace prefix, namespace)
				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
				SOAPHeaderElement element = sh.addHeaderElement(name);
				
				//add header element value
				String newValue = propertyValue + "," + TOKEN;
				element.addTextNode(newValue);
				
				System.out.printf("%s put token '%s' on response message header%n", CLASS_NAME, TOKEN);
			
			}catch(SOAPException e){
				System.out.printf("Failed to add SOAP header because of %s%n", e);
			}
        } else{
        	//inbound message
        	//get token from request SOAP header
        	try{
        		SOAPMessage msg = smc.getMessage();
        		SOAPPart sp = msg.getSOAPPart();
        		SOAPEnvelope se = sp.getEnvelope();
        		SOAPHeader sh = se.getHeader();
        		
        		if(sh == null){
        			System.out.println("Header not found");
        			return true;
        		}
        		
        		//get first header element
        		Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
        		Iterator it = sh.getChildElements(name);
        		//check header element
        		if(!it.hasNext()){
        			System.out.printf("Header element %s not found.%n", REQUEST_HEADER);
        			return true;
        		}
        		SOAPElement element = (SOAPElement) it.next();
        		//get header element value
        		String headerValue = element.getValue();
        		System.out.printf("%s got '%s'%n", CLASS_NAME, headerValue);
        		//put token in request context
        		String newValue = headerValue + "," + TOKEN;
        		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
        		smc.put(REQUEST_PROPERTY, newValue);
        		// set property scope to application so that server class can
				// access property
        		smc.setScope(REQUEST_PROPERTY, Scope.APPLICATION);
        	}catch(SOAPException e){
				System.out.printf("Failed to get SOAP header because of %s%n", e);
        	}
        }
        return true;
    }
        

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }

}