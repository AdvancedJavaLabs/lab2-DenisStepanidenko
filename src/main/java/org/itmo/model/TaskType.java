package org.itmo.model;

import lombok.Getter;

@Getter
public enum TaskType {

    COUNT_OF_WORD("request-count-of-words", "result-count-of-words"),
    FIND_TOP_N_WORDS("request-find-top-n-words", "result-find-top-n-words");


    private final String kafkaTopicRequest;
    private final String kafkaTopicResult;

    TaskType(String kafkaTopicRequest, String kafkaTopicResult) {

        this.kafkaTopicRequest = kafkaTopicRequest;
        this.kafkaTopicResult = kafkaTopicResult;
    }

}
