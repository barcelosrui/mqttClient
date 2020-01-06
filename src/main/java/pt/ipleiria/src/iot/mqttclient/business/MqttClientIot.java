package pt.ipleiria.src.iot.mqttclient.business;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;

public class MqttClientIot {

    private MQTT mqtt = new MQTT();
    private BlockingConnection connection;

    public MqttClientIot(String serverURI) throws Exception {
        mqtt.setHost(serverURI);
        connection = mqtt.blockingConnection();
        connection.connect();
    }

    public void publish(String topic, String payload) throws Exception {
        connection.publish(topic, payload.getBytes(), QoS.AT_LEAST_ONCE, false);
    }

    public String subscribe(String topic) throws Exception {
        Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics);


        Message message = connection.receive();

        //connection.disconnect();
        return new String(message.getPayload());
    }

}
