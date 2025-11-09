package org.itmo.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic countOfWordsTopic() {
        return new NewTopic("request-count-of-words", 3, (short) 1);
    }


    @Bean
    public NewTopic findingTopNWordsTopic() {
        return new NewTopic("request-find-top-n-words", 3, (short) 1);
    }

    @Bean
    public NewTopic sortingAllSentencesByCountOfSymbols() {
        return new NewTopic("request-sorting-all-sentences-by-count-of-symbols", 3, (short) 1);
    }


}