package utn.frba.wordle.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpSolutionDto {

    Set<String> possibleWords;
}
