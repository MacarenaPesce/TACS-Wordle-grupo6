package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.TournamentEntity;

import java.util.List;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    @Query(value = "select * from tournament where type = 'PUBLIC'", nativeQuery = true)
    List<TournamentEntity> getPublicTournaments();

    @Query(value = "select * from tournament where name = :name and state = 'READY'", nativeQuery = true)
    TournamentEntity findByName(String name);

    @Query(value = "select t.* from registration r, tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and t.state = :state", nativeQuery = true)
    List<TournamentEntity> findUserTournamentsByState(Long userId, String state);

    @Query(value = "select t.* from registration r, tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id\n" +
            "and state in('READY', 'STARTED')", nativeQuery = true)
    List<TournamentEntity> findActiveTournamentsFromUser(Long userId);

}
