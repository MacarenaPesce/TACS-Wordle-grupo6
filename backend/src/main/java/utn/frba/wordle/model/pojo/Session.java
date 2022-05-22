package utn.frba.wordle.model.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Session {

    private String token;
    private String username;
    private String email;
    private Long userId;

}
