package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;

import java.util.List;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    @Modifying
    @Query(value = "insert into Tournament_User (Id_Tournament, Id_User) values (:tournamentId, :userId)", nativeQuery = true)
    void addMember(Long tournamentId, Long userId);

    @Query(value = "select * from Tournament where type = 'PUBLIC'", nativeQuery = true)
    List<TournamentEntity> getPublicTournaments();

    @Query(value = "select * from Tournament where name = :name and state = 'ACTIVE'", nativeQuery = true)
    TournamentEntity findByName(String name);
}
