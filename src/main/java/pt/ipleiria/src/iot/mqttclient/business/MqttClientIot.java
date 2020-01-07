package pt.ipleiria.src.iot.mqttclient.business;

import org.fusesource.mqtt.client.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MqttClientIot {

    private MQTT mqtt = new MQTT();
    private BlockingConnection connection;

    public MqttClientIot(String serverURI) throws Exception {
        mqtt.setHost(serverURI);
        connection = mqtt.blockingConnection();
    }

    public MqttClientIot(String serverURI, String userName, String password, boolean isSecure) throws Exception {
        mqtt.setUserName(userName);
        mqtt.setPassword(password);
        mqtt.setHost(serverURI);

        if (isSecure) {
            SSLContext sslContext = sslContext();
            mqtt.setSslContext(sslContext);
        }
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

    private SSLContext sslContext() throws GeneralSecurityException, IOException {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("/ca.crt");
        if (in == null) {
            return null;
        }

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(in);

        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null); // You don't need the KeyStore instance to come from a file.
        ks.setCertificateEntry("caCert", caCert);

        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }
}