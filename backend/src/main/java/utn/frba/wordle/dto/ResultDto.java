package utn.frba.wordle.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ResultDto {
    Integer  result;
    String languaje;
}