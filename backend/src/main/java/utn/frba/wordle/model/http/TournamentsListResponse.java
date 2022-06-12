package utn.frba.wordle.model.http;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class TournamentsListResponse {
    List<TournamentResponse> tournaments;
    private final Integer maxResults;
    private final Integer pageNumber;
    private final Integer totalPages;
}
