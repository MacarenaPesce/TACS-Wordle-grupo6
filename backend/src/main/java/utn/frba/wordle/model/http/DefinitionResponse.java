package utn.frba.wordle.model.http;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class DefinitionResponse {
    List<String> definitions;
}
