package org.itmo;

import org.itmo.consumer.KafkaConsumer;
import org.itmo.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Main {

    private static String path = "C:\\Users\\stepa\\IdeaProjects\\lab2-DenisStepanidenko\\Text1.txt";

    public static void main(String[] args) throws IOException {

        ApplicationContext context = SpringApplication.run(Main.class, args);
        ProcessingService processingService = context.getBean(ProcessingService.class);

        processingService.initialize(path);

        processingService.countOfWords();
    }


}
