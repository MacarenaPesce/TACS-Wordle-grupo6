package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Builder
public class RegisterRequest {

    private final String username;
    private final String password;
    private final String email;

    @Override
    public String toString(){
        return String.format("{username: %s, email: %s}", username, email);
    }
}
