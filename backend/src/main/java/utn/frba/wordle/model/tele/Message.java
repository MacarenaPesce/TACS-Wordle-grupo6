package utn.frba.wordle.model.tele;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Message {
    private final Integer message_id;
    private final User from;
    private final Chat chat;
    private final Long date;
    private final String text;
}
