package utn.frba.wordle.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class MemberNewDto {

    private String username;
    private Long tournamentId;
}
