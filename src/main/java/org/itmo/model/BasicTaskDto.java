package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BasicTaskDto {

    private TextSection textSection;
    private int idOperation;

}
