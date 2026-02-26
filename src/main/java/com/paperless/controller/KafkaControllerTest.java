package com.paperless.controller;

import com.paperless.config.KafkaProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class KafkaControllerTest {

    private final KafkaProducer kafkaProducer;

    public KafkaControllerTest(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        kafkaProducer.sendToOcrQueue(message);
        return "testmsg to ocr-queue: " + message;
    }
}
