package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.enums.Language;

import java.time.LocalDate;
import java.util.List;

public interface PunctuationRepository extends CrudRepository<PunctuationEntity, Long> {


    @Query(value = "select p from PunctuationEntity p \n" +
            "where p.user.id = :userId \n" +
            "and p.language = :language \n" +
            "and p.date = :now")
    List<PunctuationEntity> findTodayResults(Long userId, LocalDate now, Language language);

    @Query(value = "select r.punctuations from RegistrationEntity r \n" +
            "where r.tournament.id = :idTournament \n")
    List<PunctuationEntity> findResultsFromTournament(Long idTournament);
}
