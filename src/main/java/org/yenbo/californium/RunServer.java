package org.yenbo.californium;

import org.eclipse.californium.core.CoapServer;

public class RunServer {

	public static void main(String[] args) throws InterruptedException {
		
		CoapDemoResource resource = new CoapDemoResource("coapDemo");
		
		CoapServer server = new CoapServer();
		server.add(resource);
		server.start();
		
		while(true) {
			// for observation test
			Thread.sleep(100);
			resource.changed();
		}
	}
}
