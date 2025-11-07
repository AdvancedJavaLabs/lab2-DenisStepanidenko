package org.itmo.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    public void send(String topic, String message) {
        System.out.println("Отправка сообщения в Kafka со стороны producer");
        kafkaTemplate.send(topic, message);
    }


}
