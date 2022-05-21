package utn.frba.wordle.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class UserDto {

    private Long id;
    private String username;
    private String email;
}
