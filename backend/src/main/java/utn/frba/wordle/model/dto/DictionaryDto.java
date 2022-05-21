package utn.frba.wordle.model.dto;

import lombok.*;
import utn.frba.wordle.model.pojo.Language;

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





