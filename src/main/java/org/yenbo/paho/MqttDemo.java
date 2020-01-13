package org.yenbo.paho;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttDemo {

	private static final Logger log = LoggerFactory.getLogger(MqttDemo.class);
	
	public static final String HOST = "tcp://localhost:1883";
	public static final String TOPIC = "timestamp";
	public static final int LOOP = 10;
	public static final int SLEEP = 1000;
	
	public static void main(String[] args) {

		MemoryPersistence persistence = new MemoryPersistence();
		
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setCleanSession(true);
		connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		
		try {
			MqttClient client = new MqttClient(HOST, UUID.randomUUID().toString(), persistence);
			
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					log.info("Message arrived. topic: {}, QoS: {}, payload: {}", topic, message.getQos(),
							new String(message.getPayload()));
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					log.info("Deliver complete. topics: {}", Arrays.toString(token.getTopics()));
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					log.error(cause.getMessage(), cause);
				}
			});
			
			client.connect(connectOptions);
			
			client.subscribe(TOPIC);
			
			for (int i = 0; i < LOOP; i++) {
				
				MqttMessage message = new MqttMessage(ZonedDateTime.now().toString().getBytes());
				message.setQos(0);
				client.publish(TOPIC, message);
				
				Thread.sleep(SLEEP);
				log.debug("sleep completed. {} milliseconds", SLEEP);
			}
			
			client.disconnect();
			client.close();
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}
}
