package utn.frba.wordle.model.tele;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Chat {
    private final Long id;
    private final String first_name;
}
