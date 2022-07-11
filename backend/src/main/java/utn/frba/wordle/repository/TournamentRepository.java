package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.TournamentEntity;

import java.util.List;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    @Query(value = "select * from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> getPublicActiveTournaments(Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish", nativeQuery = true)
    Integer listPublicActiveTournamentsTotalPages();

    @Query(value = "select * from wordle.tournament \n" +
            "where id = :id \n" +
            "and curdate() < finish \n", nativeQuery = true)
    TournamentEntity getActiveTournamentsById(Long id);

    @Query(value = "select * from wordle.tournament \n" +
            "where lower(name) = lower(:name) \n" +
            "and curdate() < finish \n", nativeQuery = true)
    TournamentEntity getActiveTournamentsByName(String name);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < finish \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> getActiveTournamentsFromUser(Long userId, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id \n" +
            "and curdate() < finish", nativeQuery = true)
    Integer getActiveTournamentsFromUserTotalPages(Long userId);

    @Query(value = "select t.* from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id\n" +
            "and curdate() < finish \n" +
            "and LOWER(t.name) like %:tournamentName% \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> findActiveTournamentsFromUser(Long userId, String tournamentName, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id\n" +
            "and curdate() < finish \n" +
            "and LOWER(t.name) like %:name%", nativeQuery = true)
    Integer findActiveTournamentsFromUserTotalPages(Long userId, String name);

    @Query(value = "select * from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish \n" +
            "and LOWER(name) like %:name% \n" +
            "LIMIT :offset,:maxResults", nativeQuery = true)
    List<TournamentEntity> findPublicActiveTournamentsByName(String name, Integer offset, Integer maxResults);

    @Query(value = "select count(*) from wordle.tournament \n" +
            "where type = 'PUBLIC' \n" +
            "and curdate() < finish \n" +
            "and LOWER(name) like %:name%", nativeQuery = true)
    Integer findPublicActiveTournamentsByNameTotalPages(String name);

    @Query(value = "select t.id from wordle.registration r, wordle.tournament t \n" +
            "where r.id_user = :userId \n" +
            "and r.id_tournament = t.id", nativeQuery = true)
    List<Long> findIdTournamentsFromUser(Long userId);
}
