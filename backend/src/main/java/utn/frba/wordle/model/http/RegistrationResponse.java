package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class NewMemberResponse {

    private String username;
    private Long tournamentId;
}
