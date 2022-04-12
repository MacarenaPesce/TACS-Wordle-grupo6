package utn.frba.wordle.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode
public class LoginDto {

    private String username;
    private String password;
    private String mail;

}
