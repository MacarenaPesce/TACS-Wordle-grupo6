package utn.frba.wordle.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import utn.frba.wordle.model.enums.Language;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class ResultDto {
    Long userId;
    Long result;
    Language language;
}

