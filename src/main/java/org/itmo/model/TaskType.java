package org.itmo.model;

import lombok.Getter;

@Getter
public enum TaskType {

    COUNT_OF_WORD("request-count-of-words", "result-count-of-words"),
    FIND_TOP_N_WORDS("request-find-top-n-words", "result-find-top-n-words"),
    SORTING_ALL_SENTENCES_BY_COUNT_OF_SYMBOLS("request-sorting-all-sentences-by-count-of-symbols", "result-sorting-all-sentences-by-count-of-symbols"),
    ANALYZE_SENTIMENTAL("request-analyze-sentimental", "result-analyze-sentimental"),
    REPLACE_ALL_NAMES("request-replace-all-names", "result-replace-all-names");


    private final String kafkaTopicRequest;
    private final String kafkaTopicResult;

    TaskType(String kafkaTopicRequest, String kafkaTopicResult) {

        this.kafkaTopicRequest = kafkaTopicRequest;
        this.kafkaTopicResult = kafkaTopicResult;
    }

}
