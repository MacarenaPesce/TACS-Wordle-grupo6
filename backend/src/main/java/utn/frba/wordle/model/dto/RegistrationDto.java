package utn.frba.wordle.model.dto;

import lombok.*;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.UserEntity;

import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class RegistrationDto {

    private final Long id;
    private final UserEntity user;
    private final Long tournamentId;
    private final Date registered;
    private final List<PunctuationEntity> punctuations;
    private Long totalScore;
    private Long daysPlayed;

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public void setDaysPlayed(Long daysPlayed) {
        this.daysPlayed = daysPlayed;
    }
}
