package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String username;
    private String password;
    private String email;

    @Override
    public String toString(){
        return String.format("{username: %s, email: %s}", username, email);
    }
}
