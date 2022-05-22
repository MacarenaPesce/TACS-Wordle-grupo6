package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;

@Getter
@Builder
@ToString
public class TournamentResponse {
    private final Long tourneyId;
    private final String name;
    private final Language language;
    private final TournamentType type;
    private final State state;
    private final Date start;
    private final Date finish;
    private final UserDto owner;


}
