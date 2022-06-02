package utn.frba.wordle.model.tele;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class User {
    private final Long id;
    private final boolean is_bot;
    private final String first_name;
}
