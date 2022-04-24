package utn.frba.wordle.dto;

import jdk.jshell.Snippet;
import lombok.*;

import java.util.ArrayList;

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
