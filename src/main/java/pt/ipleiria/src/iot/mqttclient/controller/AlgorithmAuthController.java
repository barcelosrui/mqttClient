package pt.ipleiria.src.iot.mqttclient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ipleiria.src.iot.mqttclient.business.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/algorithm/auth")
public class AlgorithmAuthController {
    private MqttClientIot mqttClientIot = new MqttClientIot("tcp://localhost:1883","mqtt","mqtt");

    private static String topic = "alert";

    public AlgorithmAuthController() throws Exception {

    }

    @GetMapping("/plain")
    public ResponseEntity<String> getPlain() throws Exception {
        return ResponseEntity.ok(mqttClientIot.subscribe(topic));
    }

    @GetMapping("/aes128ecb")
    public ResponseEntity<String> getAes128Ecb() throws Exception {
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));
        String myKey = "abcdefghijklmnop";
        return ResponseEntity.ok(new String(Aes128Ecb.decryptMessage(cipher, myKey.getBytes(StandardCharsets.UTF_8))));
    }

    @GetMapping("/3desecb")
    public ResponseEntity<String> get3Des() throws Exception {
        String myKey = "abcdefghijklmnop12345678";
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));

        return ResponseEntity.ok(new String(TripleDesEcb.decryptMessage(cipher, myKey.getBytes("UTF-8"))));
    }

    @GetMapping("/desecb")
    public ResponseEntity<String> getDes() throws Exception {
        String myKey = "abcdefgh";
        byte[] cipher = IotUtils.asBytes(mqttClientIot.subscribe(topic));

        return ResponseEntity.ok(new String(DesEcb.decryptMessage(cipher, myKey.getBytes("UTF-8"))));
    }

}
