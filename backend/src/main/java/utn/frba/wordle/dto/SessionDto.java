package utn.frba.wordle.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SessionDto {

    private String token;
    private String username;
    private String email;
    private Long userId;

}
