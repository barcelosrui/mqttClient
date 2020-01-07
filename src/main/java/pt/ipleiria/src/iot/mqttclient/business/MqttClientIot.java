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
    }

    public MqttClientIot(String serverURI, String userName, String password) throws Exception {

        mqtt.setUserName(userName);
        mqtt.setPassword(password);
        mqtt.setHost(serverURI);
        connection = mqtt.blockingConnection();

    }

    public void publish(String topic, String payload) throws Exception {
        connection.connect();
        connection.publish(topic, payload.getBytes(), QoS.AT_LEAST_ONCE, false);
        connection.disconnect();
    }

    public String subscribe(String topic) throws Exception {
        connection.connect();
        Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics);

        Message message = connection.receive();

        connection.disconnect();
        return new String(message.getPayload());
    }

}
