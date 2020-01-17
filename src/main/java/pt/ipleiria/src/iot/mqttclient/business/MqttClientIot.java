package pt.ipleiria.src.iot.mqttclient.business;

import org.fusesource.mqtt.client.*;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MqttClientIot {

    private MQTT mqtt = new MQTT();

    public MqttClientIot(String serverURI) throws Exception {
        mqtt.setHost(serverURI);
    }

    public MqttClientIot(String serverURI, String userName, String password, boolean isSecure) throws Exception {
        mqtt.setUserName(userName);
        mqtt.setPassword(password);
        mqtt.setHost(serverURI);
        if (isSecure) {
            SSLContext sslContext = IotUtils.getSslContextForCertificateFile("ca.crt");
            mqtt.setSslContext(sslContext);
        }

    }

    public void publish(String topic, String payload) throws Exception {
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();
        connection.publish(topic, payload.getBytes(), QoS.AT_LEAST_ONCE, false);
        connection.disconnect();
    }

    public String subscribe(String topic) throws Exception {
        BlockingConnection newConnection = mqtt.blockingConnection();
        newConnection.connect();
        Topic[] topics = {new Topic(topic, QoS.AT_LEAST_ONCE)};
        newConnection.subscribe(topics);
        Message message = newConnection.receive();
        newConnection.disconnect();
        return new String(message.getPayload());
    }


}