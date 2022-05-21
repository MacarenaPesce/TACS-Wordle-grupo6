package utn.frba.wordle.model.http;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpResponse {

    Set<String> possibleWords;
}
