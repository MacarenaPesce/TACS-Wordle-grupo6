package utn.frba.wordle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity @IdClass(RankingEntity.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Ranking")
public class RankingEntity implements Serializable {

    @Id
    private Long position;

    @Id
    private Long totalScore;

    @Id
    private String username;

    @Id
    private Long idTournament;
}
