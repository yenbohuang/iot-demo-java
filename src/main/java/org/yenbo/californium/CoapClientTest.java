package org.yenbo.californium;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapClientTest {

	private static final Logger log = LoggerFactory.getLogger(CoapClientTest.class);
	
	public static void main(String[] args) {
		
		try {
			
			if (ping()) {
				
				discover();
				
				log.info("--- Create client ----");
				CoapClient client = new CoapClient("coap://localhost:5683/coapDemo");
				log.info("URI: {}", client.getURI());
				
				getSync(client);
				getAsync(client);
				observe(client);
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	private static void printResponse(CoapResponse response) {
		
		log.info("Code: {}", response.getCode().toString());
		log.info("Payload: {}", response.getResponseText());
	}
	
	private static void printWebLink(WebLink link) {
		
		log.info("WebLink URI: {}", link.getURI());
		link.getAttributes().getInterfaceDescriptions();
		
		log.info("WebLink Attribute ContentTypes: {}",
				link.getAttributes().getContentTypes().toString());
		log.info("WebLink Attribute keys: {}", link.getAttributes().getAttributeKeySet().toString());
		log.info("WebLink Attribute Count: {}", link.getAttributes().getCount());
		log.info("WebLink Attribute Interface Descriptions: {}",
				link.getAttributes().getInterfaceDescriptions().toString());
		log.info("WebLink Attribute MaximumSizeEstimate: {}",
				link.getAttributes().getMaximumSizeEstimate());
		log.info("WebLink Attribute ResourceTypes: {}",
				link.getAttributes().getResourceTypes().toString());
		log.info("WebLink Attribute Title: {}", link.getAttributes().getTitle());
	}
	
	private static void sleep1s() throws InterruptedException {
		log.info("sleep for 1 second");
		Thread.sleep(1000);
		log.info("sleep ended");
	}
	
	public static void getSync(CoapClient client) throws ConnectorException, IOException {
		
		log.info("--- Synchronous GET ----");
		
		printResponse(client.get());
	}
	
	public static void getAsync(CoapClient client) throws InterruptedException {
		
		log.info("--- Asynchronous GET ----");
				
		client.get(new CoapHandler() {
			
			@Override
			public void onLoad(CoapResponse response) {
				printResponse(response);
			}
			
			@Override
			public void onError() {
				log.error("Get failed");	
			}
		});
		
		sleep1s();
	}
	
	public static void discover() throws ConnectorException, IOException {
		
		log.info("--- Discovery ----");
		
		CoapClient client = new CoapClient("localhost");
		
		for (WebLink link: client.discover()) {
			printWebLink(link);
		}
	}
	
	public static boolean ping() {
		
		log.info("--- Ping ----");
		
		CoapClient client = new CoapClient("localhost");
		boolean result = client.ping();
		
		log.info("Ping: {}", result);
		return result;
	}
	
	public static void observe(CoapClient client) throws InterruptedException {
		
		log.info("--- Observe ----");
		
		client.observe(new CoapHandler() {
			
			@Override
			public void onLoad(CoapResponse response) {
				printResponse(response);
			}
			
			@Override
			public void onError() {
				log.error("Observe failed");
			}
		});
		
		sleep1s();
	}
}
