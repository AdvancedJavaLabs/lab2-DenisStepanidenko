package org.itmo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatorReplaceNamesDto {

    private List<String> processedSentences;
    private int idOperation;
    private int sectionIndex;

}