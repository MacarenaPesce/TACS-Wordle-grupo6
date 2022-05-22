package utn.frba.wordle.model.http;

import lombok.*;

@Getter
@Builder
public class LoginRequest {

    private final String username;
    private final String password;

    @Override
    public String toString(){
        return String.format("{username: %s}", username);
    }
}
