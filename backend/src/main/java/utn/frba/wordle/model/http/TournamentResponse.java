package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class TournamentResponse {
    Long tourneyId;
    String name;
    Language language;
    TournamentType type;
    State state;
    Date start;
    Date finish;
    UserDto owner;


}
