package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.TournamentEntity;

import java.util.List;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    @Query(value = "select * from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish", nativeQuery = true)
    List<TournamentEntity> getPublicActiveTournaments();

    @Query(value = "select * from wordle.tournament \n" +
            "where name = :name \n" +
            "and curdate() < finish \n", nativeQuery = true)
    TournamentEntity getActiveTournamentsByName(String name);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < finish", nativeQuery = true)
    List<TournamentEntity> getActiveTournamentsFromUser(Long userId);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id\n" +
            "and curdate() < finish \n" +
            "and LOWER(t.name) like %:tournamentName%", nativeQuery = true)
    List<TournamentEntity> findActiveTournamentsFromUser(Long userId, String tournamentName);

    @Query(value = "select * from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish \n" +
            "and LOWER(name) like %:name%", nativeQuery = true)
    List<TournamentEntity> findPublicActiveTournamentsByName(String name);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < t.start \n" +
            "order by name asc \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> findUserReadyTournaments(Long userId, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < t.start ", nativeQuery = true)
    Integer userTournamentsReadyTotalPages(Long userId);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < t.finish \n" +
            "and t.start < curdate() \n" +
            "order by name asc \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> findUserStartedTournaments(Long userId, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < t.finish \n" +
            "and t.start < curdate()", nativeQuery = true)
    Integer userTournamentsStartedTotalPages(Long userId);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and t.finish < curdate() \n" +
            "order by name asc \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> findUserFinishedTournaments(Long userId, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and t.finish < curdate()", nativeQuery = true)
    Integer userTournamentsFinishedTotalPages(Long userId);
}
