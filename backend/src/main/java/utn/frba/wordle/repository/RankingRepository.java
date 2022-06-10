package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.RankingEntity;

import java.util.Set;

public interface RankingRepository extends CrudRepository<RankingEntity, Long> {

    @Query(value = "SELECT r.* FROM wordle.Ranking r \n" +
            "where r.id_Tournament = :idTournament \n" +
            "order by position asc", nativeQuery = true)
    Set<RankingEntity> getScores(Long idTournament);

    @Query(value= "SELECT r from RankingEntity r \n" +
            "where r.idTournament = :idTournament \n" +
            "and r.username = :username")
    RankingEntity findScore(Long idTournament, String username);
}
