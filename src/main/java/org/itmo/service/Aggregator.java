package org.itmo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.itmo.model.AggregatorAllSentencesSortingByCountOfSymbols;
import org.itmo.model.AggregatorDtoCountOfWords;
import org.itmo.model.AggregatorTopNWordsDto;
import org.itmo.model.TaskType;
import org.itmo.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Aggregator {

    private Map<Integer, Long> aggregationResultCountingOfWords = new ConcurrentHashMap<>();

    private Map<Integer, Map<String, Integer>> aggregationResultFindingTopNWords = new ConcurrentHashMap<>();
    private Map<Integer, Map<String, Integer>> aggregationResultAllSentencesSortingByCountOfSymbols = new ConcurrentHashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaProducer kafkaProducer;


    public void aggregateCalculatingCountOfWords(AggregatorDtoCountOfWords requestCountingWords) {

        aggregationResultCountingOfWords.compute(requestCountingWords.getIdOperation(), (key, value) -> {

            if (value == null) {
                return requestCountingWords.getCount();
            }

            return value + requestCountingWords.getCount();

        });

        FacadeProcessingService.getCompletionOfOperations().computeIfPresent(requestCountingWords.getIdOperation(), (key, value) -> {

            int newValue = value - 1;

            if (newValue <= 0) {

                AggregatorDtoCountOfWords result = new AggregatorDtoCountOfWords(aggregationResultCountingOfWords.get(requestCountingWords.getIdOperation()), requestCountingWords.getIdOperation());

                try {
                    String message = objectMapper.writeValueAsString(result);

                    kafkaProducer.send(TaskType.COUNT_OF_WORD.getKafkaTopicResult(), message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                aggregationResultCountingOfWords.remove(requestCountingWords.getIdOperation());


                return null;
            }

            return newValue;
        });


    }

    public void aggregateFindingTopNWords(AggregatorTopNWordsDto requestFindingTopNWords) {


        aggregationResultFindingTopNWords.compute(requestFindingTopNWords.getIdOperation(), (key, value) -> {

            if (Objects.isNull(value)) {
                value = new HashMap<>();

                value.putAll(requestFindingTopNWords.getTopNWords());

                return value;
            }


            for (Map.Entry<String, Integer> entry : requestFindingTopNWords.getTopNWords().entrySet()) {

                if (value.containsKey(entry.getKey())) {
                    value.put(entry.getKey(), value.get(entry.getKey()) + entry.getValue());
                } else {
                    value.put(entry.getKey(), entry.getValue());
                }

            }

            return value;

        });

        FacadeProcessingService.getCompletionOfOperations().computeIfPresent(requestFindingTopNWords.getIdOperation(), (key, value) -> {

            int newValue = value - 1;

            if (newValue <= 0) {

                Map<String, Integer> resultMap = aggregationResultFindingTopNWords.get(requestFindingTopNWords.getIdOperation())
                        .entrySet()
                        .stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .limit(requestFindingTopNWords.getN())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));


                AggregatorTopNWordsDto result = new AggregatorTopNWordsDto(resultMap, requestFindingTopNWords.getN(), requestFindingTopNWords.getIdOperation());

                try {
                    String message = objectMapper.writeValueAsString(result);

                    kafkaProducer.send(TaskType.FIND_TOP_N_WORDS.getKafkaTopicResult(), message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                aggregationResultFindingTopNWords.remove(requestFindingTopNWords.getIdOperation());


                return null;
            }

            return newValue;
        });


    }

    public void aggregateAllSortingSentencesByCountOfSymbols(AggregatorAllSentencesSortingByCountOfSymbols requestSortingSentencesByCountOfSymbols) {

        aggregationResultAllSentencesSortingByCountOfSymbols.compute(requestSortingSentencesByCountOfSymbols.getIdOperation(), (key, value) -> {

            if (value == null) {
                value = new HashMap<>();

                value.putAll(requestSortingSentencesByCountOfSymbols.getAllSentences());
                return value;
            }

            for (Map.Entry<String, Integer> entry : requestSortingSentencesByCountOfSymbols.getAllSentences().entrySet()) {

                if (!value.containsKey(entry.getKey())) {
                    value.put(entry.getKey(), entry.getValue());
                }
            }

            return value;

        });

        FacadeProcessingService.getCompletionOfOperations().computeIfPresent(requestSortingSentencesByCountOfSymbols.getIdOperation(), (key, value) -> {

            int newValue = value - 1;

            if (newValue <= 0) {

                Map<String, Integer> resultMap = aggregationResultAllSentencesSortingByCountOfSymbols.get(requestSortingSentencesByCountOfSymbols.getIdOperation())
                        .entrySet()
                        .stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));


                AggregatorAllSentencesSortingByCountOfSymbols result = new AggregatorAllSentencesSortingByCountOfSymbols(resultMap, requestSortingSentencesByCountOfSymbols.getIdOperation());

                try {
                    String message = objectMapper.writeValueAsString(result);

                    kafkaProducer.send(TaskType.SORTING_ALL_SENTENCES_BY_COUNT_OF_SYMBOLS.getKafkaTopicResult(), message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                aggregationResultAllSentencesSortingByCountOfSymbols.remove(requestSortingSentencesByCountOfSymbols.getIdOperation());


                return null;
            }

            return newValue;
        });

    }
}
