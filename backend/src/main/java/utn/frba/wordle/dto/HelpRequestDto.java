package utn.frba.wordle.dto;

import lombok.*;

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
