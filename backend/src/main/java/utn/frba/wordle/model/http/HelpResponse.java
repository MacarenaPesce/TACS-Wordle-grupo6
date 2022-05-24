package utn.frba.wordle.model.http;

import lombok.*;

import java.util.Set;

@Getter
@Builder
@ToString
public class HelpResponse {

    private final Set<String> possibleWords;
}
