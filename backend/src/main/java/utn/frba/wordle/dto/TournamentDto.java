package utn.frba.wordle.dto;

import lombok.*;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class TournamentDto {

    Integer tourneyId;
    String name;
    Language language;
    TounamentType type;
    Date start;
    Date finish;
}
