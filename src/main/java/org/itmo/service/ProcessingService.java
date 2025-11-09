package org.itmo.service;

import org.itmo.model.AggregatorDtoCountOfWords;
import org.itmo.model.AggregatorTopNWordsDto;
import org.itmo.model.CalculatingCountOfWordsTaskDto;
import org.itmo.model.FindTopNWordsTaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProcessingService {

    @Autowired
    private Aggregator aggregator;


    public void calculateCountOfWords(CalculatingCountOfWordsTaskDto calculatingCountOfWordsTaskDto) {

        long count = 0;

        for (String str : calculatingCountOfWordsTaskDto.getTextSection().getSentences()) {

            if (str.isEmpty()) continue;

            count += str.split("\\s+").length;
        }

        aggregator.aggregateCalculatingCountOfWords(new AggregatorDtoCountOfWords(count, calculatingCountOfWordsTaskDto.getIdOperation()));

    }


    public void findTopNWords(FindTopNWordsTaskDto findTopNWordsTaskDto) {

        Map<String, Integer> topWords = new HashMap<>();

        for (String sentence : findTopNWordsTaskDto.getTextSection().getSentences()) {

            String[] words = sentence.split("\\s+");

            for (String word : words) {

                String cleanedWord = word.replaceAll("[^a-zA-Zа-яА-Я]", "");

                if (topWords.containsKey(cleanedWord.toLowerCase())) {
                    topWords.put(cleanedWord.toLowerCase(), topWords.get(cleanedWord.toLowerCase()) + 1);
                } else {
                    topWords.put(cleanedWord.toLowerCase(), 1);
                }

            }


        }

        aggregator.aggregateFindingTopNWords(new AggregatorTopNWordsDto(topWords, findTopNWordsTaskDto.getN(), findTopNWordsTaskDto.getIdOperation()));

    }
}
