package pt.upa.ca.ws;

import java.security.PublicKey;
import java.security.cert.Certificate;

import pt.upa.ca.ws.handler.HeaderHandler;

public class CAPort{
	private HeaderHandler handler = new HeaderHandler();
	private PublicKey publicKey;
	private Certificate certificate;
	
	public CAPort(){
		
	}
	
	public PublicKey getPublicKey(String name) throws Exception{
		String certificateName = name + ".cer";
		String certificatePath = "src/main/resources/" + certificateName;
		certificate = handler.readCertificateFile(certificatePath);
		publicKey = certificate.getPublicKey();
		return publicKey;
	}
}