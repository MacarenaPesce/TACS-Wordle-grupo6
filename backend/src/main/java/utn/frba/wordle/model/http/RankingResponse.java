package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.pojo.Punctuation;

import java.util.List;

@Getter
@Builder
@ToString
public class RankingResponse {
    private final List<Punctuation> punctuations;
    private final Long idTournament;
}
