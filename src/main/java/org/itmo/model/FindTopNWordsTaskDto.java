package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindTopNWordsTaskDto extends BasicTaskDto {

    private int n;

    public FindTopNWordsTaskDto(TextSection textSection, int idOperation, int n) {
        super(textSection, idOperation);
        this.n = n;
    }



}
