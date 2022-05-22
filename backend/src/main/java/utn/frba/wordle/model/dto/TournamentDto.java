package utn.frba.wordle.model.dto;

import lombok.*;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class TournamentDto {

    Long tourneyId;
    String name;
    Language language;
    TournamentType type;
    State state;
    Date start;
    Date finish;
    UserDto owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TournamentDto)) return false;
        TournamentDto that = (TournamentDto) o;
        return Objects.equals(tourneyId, that.tourneyId) &&
                Objects.equals(name, that.name) &&
                language == that.language &&
                type == that.type &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourneyId, name, language, type, owner);
    }
}
