package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceTaskDto extends BasicTaskDto {

    private String replaceWord;
    private int sectionId;

    public ReplaceTaskDto(TextSection textSection, int idOperation, String replaceWord, int sectionId) {
        super(textSection, idOperation);
        this.replaceWord = replaceWord;
        this.sectionId = sectionId;
    }

}
