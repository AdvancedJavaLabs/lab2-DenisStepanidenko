package org.itmo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatorTopNWordsDto {

    private Map<String, Integer> topNWords;
    private int n;
    private int idOperation;

}
