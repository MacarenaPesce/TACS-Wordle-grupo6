package utn.frba.wordle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Ranking")
public class RankingEntity implements Serializable {

    @Id
    private Long id;

    private Long position;

    private Long totalScore;

    private String username;

    private Long idTournament;

    private Date lastSubmittedScore;
}
