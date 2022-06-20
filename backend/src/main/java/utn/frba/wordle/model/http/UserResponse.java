package utn.frba.wordle.model.http;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
}
