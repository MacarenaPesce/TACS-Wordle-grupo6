package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class SubmitResultRequest {
    Long userId;
    Long result;
    Language language;
}

