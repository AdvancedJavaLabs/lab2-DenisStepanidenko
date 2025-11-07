package org.itmo.model;

import lombok.Getter;

@Getter
public enum TaskType {

    COUNT_OF_WORD("request-count-of-words", "result-count-of-words");

    private final String kafkaTopicRequest;
    private final String kafkaTopicResult;

    TaskType(String kafkaTopicRequest, String kafkaTopicResult) {

        this.kafkaTopicRequest = kafkaTopicRequest;
        this.kafkaTopicResult = kafkaTopicResult;
    }

}
