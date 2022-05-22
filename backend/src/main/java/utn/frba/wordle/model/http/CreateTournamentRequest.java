package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.TournamentType;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CreateTournamentRequest {

    String name;
    Language language;
    TournamentType type;
    Date start;
    Date finish;

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
