package utn.frba.wordle.model.pojo;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
public class Punctuation {
    private final Long position;
    private final Long punctuation;
    private final String user;
    private final Boolean submittedScoreToday;

    public String toStringTelegram(){
        return position+" - "+user+" - Puntuacion: "+punctuation;
    }
}
