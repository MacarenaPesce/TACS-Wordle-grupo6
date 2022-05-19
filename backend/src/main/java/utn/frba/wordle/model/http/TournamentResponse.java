package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.model.pojo.TournamentType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TournamentResponse {
    Long tourneyId;
    String name;
    Language language;
    TournamentType type;
    State state;
    Date start;
    Date finish;
    UserDto owner;

    public TournamentResponse(TournamentDto dto) {
        TournamentResponse.builder()
                .tourneyId(dto.getTourneyId())
                .name(dto.getName())
                .language(dto.getLanguage())
                .type(dto.getType())
                .state(dto.getState())
                .start(dto.getStart())
                .finish(dto.getFinish())
                .owner(dto.getOwner())
                .build();
    }
}
