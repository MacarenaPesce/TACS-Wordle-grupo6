package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.pojo.Language;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@ToString
public class PunctuationResponse {
    Long punctuation;
    Language language;
}
