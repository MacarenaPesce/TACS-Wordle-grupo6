package utn.frba.wordle.model.http;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
public class DefinitionResponse {
    private final List<String> definitions;
}
