package utn.frba.wordle.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Ranking {
    List<Punctuation> punctuations;
    Long idTournament;
}
