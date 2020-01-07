package pt.ipleiria.src.iot.mqttclient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ipleiria.src.iot.mqttclient.business.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/algorithm/tls")
public class AlgorithmTlsController {
    private MqttClientIot mqttClientIot = new MqttClientIot("ssl://localhost:8883", "mqtt", "mqtt", true);

    private static String topic = "alert";

    public AlgorithmTlsController() throws Exception {
    }

    @GetMapping("/plain")
    public ResponseEntity<String> getPlainTls() throws Exception {
        return ResponseEntity.ok(mqttClientIot.subscribe(topic));
    }

    @GetMapping("/aes128ecb")
    public ResponseEntity<String> getAes128EcbTls() throws Exception {
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));
        String myKey = "abcdefghijklmnop";
        return ResponseEntity.ok(new String(Aes128Ecb.decryptMessage(cipher, myKey.getBytes(StandardCharsets.UTF_8))));
    }

    @GetMapping("/3desecb")
    public ResponseEntity<String> get3DesTls() throws Exception {
        String myKey = "abcdefghijklmnop12345678";
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));

        return ResponseEntity.ok(new String(TripleDesEcb.decryptMessage(cipher, myKey.getBytes("UTF-8"))));
    }

    @GetMapping("/desecb")
    public ResponseEntity<String> getDesTls() throws Exception {
        String myKey = "abcdefgh";
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));

        return ResponseEntity.ok(new String(DesEcb.decryptMessage(cipher, myKey.getBytes("UTF-8"))));
    }

}
