package utn.frba.wordle.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class MemberDto {
    String username;
    Number tournamentId;
}
