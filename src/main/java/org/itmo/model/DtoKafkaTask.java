package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DtoKafkaTask {

    private TextSection textSection;
    private int idOperation;
    private int totalCountOfSections;


}
