package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class CreateTournamentRequest {

    private final String name;
    private final Language language;
    private final TournamentType type;
    private final String start;
    private final String finish;
}
