package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class RegistrationResponse {

    private String username;
    private Long tournamentId;
}
