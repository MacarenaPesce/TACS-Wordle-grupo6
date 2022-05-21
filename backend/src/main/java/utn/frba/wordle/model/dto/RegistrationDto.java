package utn.frba.wordle.model.dto;

import lombok.*;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.UserEntity;

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
