package utn.frba.wordle.dto;

import lombok.*;
import utn.frba.wordle.model.Language;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ResultDto {
    Long id;
    Long  result;
    Language language;
}

