package org.itmo.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import jakarta.annotation.PostConstruct;
import org.itmo.model.TextSection;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SentimentAnalysisService {

    private StanfordCoreNLP pipeline;

    @PostConstruct
    public void initialize() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        props.setProperty("tokenize.language", "en");
        pipeline = new StanfordCoreNLP(props);
    }

    public double analyzeSentiment(TextSection textSection) {
        if (pipeline == null) {
            initialize();
        }

        double totalSentiment = 0;
        int sentenceCount = 0;

        for (String sentence : textSection.getSentences()) {
            if (sentence.trim().isEmpty()) continue;

            Annotation annotation = pipeline.process(sentence);
            for (CoreMap sent : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                String sentiment = sent.get(SentimentCoreAnnotations.SentimentClass.class);
                totalSentiment += convertSentimentToScore(sentiment);
                sentenceCount++;
            }
        }

        return sentenceCount > 0 ? totalSentiment / sentenceCount : 0;
    }

    private int convertSentimentToScore(String sentiment) {
        switch (sentiment) {
            case "Very negative": return 1;
            case "Negative": return 2;
            case "Positive": return 4;
            case "Very positive": return 5;
            default: return 3;
        }
    }
}