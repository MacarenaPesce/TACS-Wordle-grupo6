package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Builder
@ToString
public class HelpRequest {

    private final String yellow;
    private final String grey;
    private final String solution;
}
