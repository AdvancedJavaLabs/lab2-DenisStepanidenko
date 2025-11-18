package org.itmo.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AggregatorSentimentalResultDto extends AggregatorSentimentalDto {

    private String sentiment;

    public AggregatorSentimentalResultDto(String sentiment, double averageSentiment, int idOperation) {
        super(averageSentiment, idOperation);
        this.sentiment = sentiment;
    }
}
