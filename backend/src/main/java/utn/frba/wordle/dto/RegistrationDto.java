package utn.frba.wordle.dto;

import lombok.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class RegistrationDto {

    private Long id;
    private UserEntity user;
    private Long tournamentId;
    private Date registered;

}
