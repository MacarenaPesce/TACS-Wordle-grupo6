package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.UserDto;

@Getter
@Builder
@ToString
public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
}
