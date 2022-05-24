package utn.frba.wordle.model.pojo;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
public class Session {

    private final String token;
    private final String username;
    private final String email;
    private final Long userId;

}
