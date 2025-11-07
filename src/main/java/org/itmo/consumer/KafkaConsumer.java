package org.itmo.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itmo.model.DtoKafkaTask;
import org.itmo.model.TaskType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "request-count-of-words", groupId = "my-group")
    public void handleCountOfWords(String message) {
        System.out.println("Hello from consumer");
        try {
            DtoKafkaTask dtoKafkaTask = objectMapper.convertValue(message, DtoKafkaTask.class);

            System.out.println("Hello from consumer");
            System.out.println(dtoKafkaTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
