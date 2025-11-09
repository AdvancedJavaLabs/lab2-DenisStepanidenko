package org.itmo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.itmo.model.AggregatorDtoCountOfWords;
import org.itmo.model.AggregatorTopNWordsDto;
import org.itmo.model.CalculatingCountOfWordsTaskDto;
import org.itmo.model.FindTopNWordsTaskDto;
import org.itmo.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProcessingService processingService;

    @org.springframework.kafka.annotation.KafkaListener(topics = "request-count-of-words", groupId = "my-group-2", concurrency = "3")
    public void handleRequestCountOfWords(String message) {

        log.debug("Пришло сообщение в consumer: {}", message);

        try {

            CalculatingCountOfWordsTaskDto calculatingCountOfWordsTaskDto = objectMapper.readValue(message, CalculatingCountOfWordsTaskDto.class);

            processingService.calculateCountOfWords(calculatingCountOfWordsTaskDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "result-count-of-words", groupId = "result-count-of-words-group", concurrency = "1")
    public void handleResultCountOfWords(String message) {

        try {
            AggregatorDtoCountOfWords result = objectMapper.readValue(message, AggregatorDtoCountOfWords.class);

            log.info("Пришёл ответ по операции с id = {}. Ответ: {}", result.getIdOperation(), result.getCount());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "request-find-top-n-words", groupId = "request-find-top-n-words-group", concurrency = "3")
    public void handleRequestFindTopNWords(String message) {
        log.debug("Пришло сообщение в consumer: {}", message);


        try {

            FindTopNWordsTaskDto findTopNWordsTaskDto = objectMapper.readValue(message, FindTopNWordsTaskDto.class);

            processingService.findTopNWords(findTopNWordsTaskDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "result-find-top-n-words", groupId = "result-find-top-n-words-group", concurrency = "1")
    public void handleResultFindingTopNWords(String message) {

        try {
            AggregatorTopNWordsDto result = objectMapper.readValue(message, AggregatorTopNWordsDto.class);

            log.info("Пришёл ответ по операции с id = {}. Топ {} слов: {}", result.getIdOperation(), result.getN(), result.getTopNWords());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
