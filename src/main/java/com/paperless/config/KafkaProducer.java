package com.paperless.config;

import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String ocrTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
            @Value("${paperless.kafka.ocr-topic}") String ocrTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.ocrTopic = ocrTopic;
    }

    public void sendToOcrQueue(String documentJson) {
        kafkaTemplate.send(ocrTopic, documentJson);
    }
}

