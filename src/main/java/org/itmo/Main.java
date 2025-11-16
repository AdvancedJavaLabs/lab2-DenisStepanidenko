package org.itmo;

import org.itmo.service.FacadeProcessingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.IOException;

@SpringBootApplication
@EnableKafka
public class Main {

    private static String path = "C:\\Users\\stepa\\IdeaProjects\\lab2-DenisStepanidenko\\Text1.txt";

    public static void main(String[] args) throws IOException {

        ApplicationContext context = SpringApplication.run(Main.class, args);
        FacadeProcessingService facadeProcessingService = context.getBean(FacadeProcessingService.class);

        facadeProcessingService.initialize(path);

        facadeProcessingService.replace("Denis");
    }


}
