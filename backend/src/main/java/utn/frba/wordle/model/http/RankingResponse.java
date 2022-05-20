package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.pojo.Punctuation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RankingResponse {
    List<Punctuation> punctuations;
    Long idTournament;
}
