package utn.frba.wordle.model.http;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class DefinitionResponse {
    List<String> definitions;
}
