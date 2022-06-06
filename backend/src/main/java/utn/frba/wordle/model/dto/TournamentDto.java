package utn.frba.wordle.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Builder
@ToString
public class TournamentDto {

    private final Long tourneyId;
    private final String name;
    private final Language language;
    private final TournamentType type;

    public void setState(State state) {
        this.state = state;
    }

    private State state;
    private final Date start;
    private final Date finish;
    private final UserDto owner;
    private final Long tournamentDuration;

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
