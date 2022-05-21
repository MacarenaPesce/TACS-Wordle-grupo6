package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    private String username;
    private String password;
    private String email;

    @Override
    public String toString(){
        return String.format("{username: %s, email: %s}", username, email);
    }
}
