package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;

import java.util.Date;
import java.util.List;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    @Modifying
    @Query(value = "insert into Registration (Id_Tournament, Id_User, registered) values (:tournamentId, :userId, :date)", nativeQuery = true)
    void addMember(Long tournamentId, Long userId, Date date);

    @Query(value = "select * from Tournament where type = 'PUBLIC'", nativeQuery = true)
    List<TournamentEntity> getPublicTournaments();

    @Query(value = "select * from Tournament where name = :name and state = 'ACTIVE'", nativeQuery = true)
    TournamentEntity findByName(String name);

    @Query(value = "select * from Tournament where owner_id = :userId and state = 'ACTIVE'", nativeQuery = true)
    List<TournamentEntity> findByUserName(Long userId);
}
