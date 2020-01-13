package org.yenbo.californium;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class CoapDemoResource extends CoapResource {

	public CoapDemoResource(String name) {
		super(name);
		setObservable(true);
		getAttributes().setTitle("CoAP demo resource");
	}

	@Override
	public void handleGET(CoapExchange exchange) {	
		exchange.respond(ZonedDateTime.now(ZoneId.of("UTC")).toString());
	}
}
