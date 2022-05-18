package utn.frba.wordle.dto;

import lombok.*;
import utn.frba.wordle.entity.PunctuationEntity;
import utn.frba.wordle.entity.UserEntity;

import java.util.Date;
import java.util.List;

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
    private List<PunctuationEntity> punctuations;

}
