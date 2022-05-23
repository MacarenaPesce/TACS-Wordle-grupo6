package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Builder
@ToString
public class RegistrationResponse {

    private final String username;
    private final Long tournamentId;
}
