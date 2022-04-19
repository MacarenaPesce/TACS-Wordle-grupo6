package utn.frba.wordle.dto;

import lombok.*;
import utn.frba.wordle.model.Language;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class DictionaryDto {
    String word;
    Language language;
    List <String> definition;
}





