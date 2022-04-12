package utn.frba.wordle.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class DictionaryDto {
    String word;
    String language;   //todo: acomodar esto
    List <String> definition;
}





