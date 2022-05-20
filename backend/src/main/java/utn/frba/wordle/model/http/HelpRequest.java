package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpRequest {

    private String yellow;
    private String grey;
    private String solution;
}
