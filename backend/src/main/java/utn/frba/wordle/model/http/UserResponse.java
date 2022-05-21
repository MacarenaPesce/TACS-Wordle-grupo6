package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.UserDto;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
}
