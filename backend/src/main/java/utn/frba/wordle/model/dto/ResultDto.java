package utn.frba.wordle.model.dto;

import lombok.*;
import utn.frba.wordle.model.pojo.Language;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ResultDto {
    Long userId;
    Long result;
    Language language;
}

