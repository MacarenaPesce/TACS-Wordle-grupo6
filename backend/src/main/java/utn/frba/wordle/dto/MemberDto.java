package utn.frba.wordle.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class MemberDto {

    private String username;
    private Integer tournamentId;
}
