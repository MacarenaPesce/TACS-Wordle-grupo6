package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.RankingEntity;

import java.util.List;

public interface RankingRepository extends CrudRepository<RankingEntity, Long> {

    @Query(value = "SELECT r FROM RankingEntity r \n" +
            "where r.idTournament = :idTournament")
    List<RankingEntity> getScores(Long idTournament);

    @Query(value= "SELECT r from RankingEntity r \n" +
            "where r.idTournament = :idTournament \n" +
            "and r.username = :username")
    RankingEntity findScore(Long idTournament, String username);
}
