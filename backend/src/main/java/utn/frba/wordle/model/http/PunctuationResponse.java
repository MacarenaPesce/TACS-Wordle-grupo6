package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;

@Getter
@Builder
@ToString
public class PunctuationResponse {
    private final Long punctuation;
    private final Language language;
}
