package org.itmo.service;

import org.itmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessingService {

    @Autowired
    private Aggregator aggregator;


    public void calculateCountOfWords(BasicTaskDto basicTaskDto) {

        long count = 0;

        for (String str : basicTaskDto.getTextSection().getSentences()) {

            if (str.isEmpty()) continue;

            count += str.split("\\s+").length;
        }

        aggregator.aggregateCalculatingCountOfWords(new AggregatorDtoCountOfWords(count, basicTaskDto.getIdOperation()));

    }


    public void findTopNWords(FindTopNWordsTaskDto findTopNWordsTaskDto) {

        Map<String, Integer> allWords = new HashMap<>();

        for (String sentence : findTopNWordsTaskDto.getTextSection().getSentences()) {

            String[] words = sentence.split("\\s+");

            for (String word : words) {

                String cleanedWord = word.replaceAll("[^a-zA-Zа-яА-Я]", "");

                if (allWords.containsKey(cleanedWord.toLowerCase())) {
                    allWords.put(cleanedWord.toLowerCase(), allWords.get(cleanedWord.toLowerCase()) + 1);
                } else {
                    allWords.put(cleanedWord.toLowerCase(), 1);
                }

            }


        }

        aggregator.aggregateFindingTopNWords(new AggregatorTopNWordsDto(allWords, findTopNWordsTaskDto.getN(), findTopNWordsTaskDto.getIdOperation()));

    }

    public void sortingAllSentenceByCountOfSymbols(BasicTaskDto basicTaskDto) {


        Map<String, Integer> allSentences = new HashMap<>();

        for (String sentence : basicTaskDto.getTextSection().getSentences()) {

            if (!allSentences.containsKey(sentence.toLowerCase())) {
                allSentences.put(sentence.toLowerCase(), sentence.toCharArray().length);
            }

        }

        aggregator.aggregateAllSortingSentencesByCountOfSymbols(new AggregatorAllSentencesSortingByCountOfSymbols(allSentences, basicTaskDto.getIdOperation()));


    }
}
