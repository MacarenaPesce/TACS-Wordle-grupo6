package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class HelpDto {

    private final String yellow;
    private final String grey;
    private final String solution;
}
