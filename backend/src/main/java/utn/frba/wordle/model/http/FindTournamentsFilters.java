package utn.frba.wordle.model.http;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;

@Getter
@Builder
@ToString
public class FindTournamentsFilters {
    String name;
    Long userId;
    TournamentType type;
    State state;
    Integer pageNumber;
    Integer maxResults;
}
