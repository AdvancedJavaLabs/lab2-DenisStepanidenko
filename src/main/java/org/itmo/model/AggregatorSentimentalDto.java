package org.itmo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatorSentimentalDto {

    private double averageSentiment;
    private int idOperation;

}
