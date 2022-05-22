package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Builder
@ToString
public class CreateTournamentRequest {

    private final String name;
    private final Language language;
    private final TournamentType type;
    private final Date start;
    private final Date finish;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateTournamentRequest)) return false;
        CreateTournamentRequest that = (CreateTournamentRequest) o;
        return Objects.equals(name, that.name) &&
                language == that.language &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, language, type);
    }
}
