package org.itmo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AggregatorAllSentencesSortingByCountOfSymbols {

    private Map<String, Integer> allSentences;
    private int idOperation;

}
