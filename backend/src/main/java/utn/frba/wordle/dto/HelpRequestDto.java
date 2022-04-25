package utn.frba.wordle.dto;

import lombok.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpRequestDto {

    private String yellow;
    private String grey;
    private String solution;
}
