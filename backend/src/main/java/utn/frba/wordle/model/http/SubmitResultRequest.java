package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;

@Getter
@Builder
@ToString
public class SubmitResultRequest {
    private final Long userId;
    private final Long result;
    private final Language language;
}

