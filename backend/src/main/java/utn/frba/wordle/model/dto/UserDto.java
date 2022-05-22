package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class UserDto {

    private final Long id;
    private final String username;
    private final String email;
}
