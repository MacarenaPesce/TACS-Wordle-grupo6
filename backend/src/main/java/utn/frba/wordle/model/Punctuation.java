package utn.frba.wordle.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Punctuation {
    String user;
    Long punctuation;
}
