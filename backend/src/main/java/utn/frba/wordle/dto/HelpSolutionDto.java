package utn.frba.wordle.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpSolutionDto {

    List<String> possibleWords = new ArrayList<>();
}
