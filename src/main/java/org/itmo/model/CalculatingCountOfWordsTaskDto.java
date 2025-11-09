package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CalculatingCountOfWordsTaskDto {

    private TextSection textSection;
    private int idOperation;

}
