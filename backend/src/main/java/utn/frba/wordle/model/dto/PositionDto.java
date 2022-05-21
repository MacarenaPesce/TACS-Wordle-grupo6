package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class PositionDto {
    Integer userId;
    String username;
    Integer points;
}
