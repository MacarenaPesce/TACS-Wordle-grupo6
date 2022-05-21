package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String username;
    private String password;

    @Override
    public String toString(){
        return String.format("{username: %s}", username);
    }
}
