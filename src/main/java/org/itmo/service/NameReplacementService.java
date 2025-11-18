package org.itmo.service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.itmo.model.ReplaceTaskDto;
import org.itmo.model.TextSection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class NameReplacementService {

    private NameFinderME nameFinder;
    private Tokenizer tokenizer;

    @PostConstruct
    public void initialize() {
        try {
            // Загрузка моделей (нужно скачать модели с сайта Apache OpenNLP)
            Resource tokenModelResource = new ClassPathResource("models/en-token.bin");
            Resource nameModelResource = new ClassPathResource("models/en-ner-person.bin");

            InputStream tokenModelStream = tokenModelResource.getInputStream();
            InputStream nameModelStream = nameModelResource.getInputStream();

            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            TokenNameFinderModel nameModel = new TokenNameFinderModel(nameModelStream);

            tokenizer = new TokenizerME(tokenModel);
            nameFinder = new NameFinderME(nameModel);

            tokenModelStream.close();
            nameModelStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize OpenNLP models", e);
        }
    }

    public List<String> replaceNames(ReplaceTaskDto taskDto) {
        List<String> processedSentences = new ArrayList<>();
        TextSection textSection = taskDto.getTextSection();
        String replacement = taskDto.getReplaceWord();

        for (String sentence : textSection.getSentences()) {
            if (sentence.trim().isEmpty()) {
                processedSentences.add(sentence);
                continue;
            }

            String processedSentence = replaceNamesInSentence(sentence, replacement);
            processedSentences.add(processedSentence);
        }

        return processedSentences;
    }

    private String replaceNamesInSentence(String sentence, String replacement) {
        try {

            String[] tokens = tokenizer.tokenize(sentence);


            Span[] nameSpans = nameFinder.find(tokens);


            if (nameSpans.length > 0) {
                String[] processedTokens = tokens.clone();

                for (Span span : nameSpans) {
                    for (int i = span.getStart(); i < span.getEnd(); i++) {
                        processedTokens[i] = replacement;
                    }
                }


                return String.join(" ", processedTokens);
            }

            return sentence;

        } catch (Exception e) {

            return sentence;
        }
    }
}