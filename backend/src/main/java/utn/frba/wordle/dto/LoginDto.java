package utn.frba.wordle.dto;

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
}
