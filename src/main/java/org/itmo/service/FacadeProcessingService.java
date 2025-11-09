package org.itmo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.itmo.model.FindTopNWordsTaskDto;
import org.itmo.producer.KafkaProducer;
import org.itmo.model.CalculatingCountOfWordsTaskDto;
import org.itmo.model.TaskType;
import org.itmo.model.TextSection;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
public class FacadeProcessingService {

    private String content;
    private List<String> allContentBySentences = new ArrayList<>();
    private List<TextSection> allTextBySections = new ArrayList<>();
    private static final int COUNT_OF_SENTENCES = 10;

    private static AtomicInteger idOperation = new AtomicInteger(0);
    private ObjectMapper objectMapper = new ObjectMapper();

    private KafkaProducer kafkaProducer;

    @Getter
    private static Map<Integer, Integer> completionOfOperations = new ConcurrentHashMap<>();

    public FacadeProcessingService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Инициализация файла с текстом.
     *
     * @param stringPath путь к файлу.
     */
    public void initialize(String stringPath) throws IOException {

        Path path = Path.of(stringPath);

        if (!Files.exists(path)) {
            throw new IOException("File not found");
        }

        content = Files.readString(path, StandardCharsets.UTF_8);

        splitContentByAllSentences();
        splitContentBySections();
    }

    /**
     * Подсчёт всего количества слов.
     */
    public void countOfWords() {

        int currentIdOperation = idOperation.incrementAndGet();
        int length = allTextBySections.size();

        completionOfOperations.put(currentIdOperation, length);

        for (TextSection textSection : allTextBySections) {

            CalculatingCountOfWordsTaskDto calculatingCountOfWordsTaskDto = new CalculatingCountOfWordsTaskDto(textSection, currentIdOperation);

            try {
                String message = objectMapper.writeValueAsString(calculatingCountOfWordsTaskDto);

                kafkaProducer.send(TaskType.COUNT_OF_WORD.getKafkaTopicRequest(), message);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void findNTopWords(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        int currentIdOperation = idOperation.incrementAndGet();
        int length = allTextBySections.size();

        completionOfOperations.put(currentIdOperation, length);

        for (TextSection textSection : allTextBySections) {

            try {

                FindTopNWordsTaskDto findTopNWordsTaskDto = new FindTopNWordsTaskDto(textSection, currentIdOperation, n);

                String message = objectMapper.writeValueAsString(findTopNWordsTaskDto);

                kafkaProducer.send(TaskType.FIND_TOP_N_WORDS.getKafkaTopicRequest(), message);


            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * Разделяет все предложения по секциям из N предложений.
     */
    private void splitContentBySections() {

        List<String> currentSections = new ArrayList<>();
        int counter = 0;

        for (String str : allContentBySentences) {

            currentSections.add(str);
            counter++;

            if (counter == COUNT_OF_SENTENCES) {
                allTextBySections.add(new TextSection(currentSections));
                currentSections = new ArrayList<>();
                counter = 0;
            }

        }

        if (!currentSections.isEmpty()) {
            allTextBySections.add(new TextSection(currentSections));
        }


    }

    /**
     * Разделяет весь текст на предложения.
     */
    private void splitContentByAllSentences() {

        List<String> sentences = new ArrayList<>();

        Pattern pattern = Pattern.compile("[^.?!]+[.?!]");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            sentences.add(matcher.group().trim());
        }


        allContentBySentences = sentences;

    }


}
