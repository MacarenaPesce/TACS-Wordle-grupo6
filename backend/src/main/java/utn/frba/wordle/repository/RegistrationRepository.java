package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.RegistrationEntity;
import utn.frba.wordle.model.entity.UserEntity;

import java.util.Date;
import java.util.List;

public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {

    @Query(value = "select r from RegistrationEntity r \n" +
            "where r.user.id = :userId ")
    List<RegistrationEntity> getAllByUser(Long userId);

    @Query(value = "select r from RegistrationEntity r \n" +
            "where r.tournament.id = :tourneyId ")
    List<RegistrationEntity> getAllByTournament(Long tourneyId);

    @Query(value = "select r from RegistrationEntity r \n" +
            "where r.user.id = :userId \n" +
            "and r.tournament.start < :dateToday \n" +
            "and r.tournament.finish > :dateToday")
    List<RegistrationEntity> getActiveRegistrationsFromUser(Long userId, Date dateToday );
}
