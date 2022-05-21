package utn.frba.wordle.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class TourneysDto {
    List<TournamentDto> tourneys;
}
