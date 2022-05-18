package utn.frba.wordle.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SessionDto {

    private String token;
    private String username;
    private String email;
    private Long userId;

}
