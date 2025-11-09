package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindTopNWordsTaskDto {

    private TextSection textSection;
    private int idOperation;
    int n;

}
