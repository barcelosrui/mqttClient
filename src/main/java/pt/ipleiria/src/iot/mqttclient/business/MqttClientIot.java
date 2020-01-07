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
            SSLContext sslContext = getSslContextForCertificateFile("ca.crt");
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

    private static SSLContext getSslContextForCertificateFile(final  String fileName) {
        try {
            KeyStore keyStore = getKeyStore(fileName);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyStore getKeyStore(final String fileName) {
        KeyStore keyStore = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            Certificate ca;
            try {
                ca = cf.generateCertificate(inputStream);
            } finally {
                inputStream.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return keyStore;
    }
}