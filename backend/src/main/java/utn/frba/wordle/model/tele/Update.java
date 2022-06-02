package utn.frba.wordle.model.tele;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Update {
    private final Long update_id;
    private final Message message;
}
