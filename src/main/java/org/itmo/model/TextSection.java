package org.itmo.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class TextSection {

    private List<String> sentences;

    public TextSection(List<String> sentences) {
        this.sentences = sentences;
    }
}
