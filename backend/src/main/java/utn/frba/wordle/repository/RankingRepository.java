package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.RankingEntity;

import java.util.Set;

public interface RankingRepository extends CrudRepository<RankingEntity, Long> {

    @Query(value = "SELECT r.* FROM wordle.ranking r \n" +
            "where r.id_Tournament = :idTournament \n" +
            "order by position asc \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    Set<RankingEntity> getScores(Long idTournament, Integer offset, Integer maxResults);

    @Query(value = "SELECT count(*) FROM wordle.ranking r \n" +
            "where r.id_Tournament = :tournamentId \n", nativeQuery = true)
    Integer getScoresTotalPages(Long tournamentId);

    @Query(value= "SELECT r from RankingEntity r \n" +
            "where r.idTournament = :idTournament \n" +
            "and r.username = :username")
    RankingEntity findScore(Long idTournament, String username);
}
