package utn.frba.wordle.model.pojo;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
public class Punctuation {
    private final String user;
    private final Long punctuation;
    private Long position;

    public void setPosition(Long position) {
        this.position = position;
    }
}
