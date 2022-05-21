package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class JoinDto {

    Number tournamentID;
    Number userID;


}
