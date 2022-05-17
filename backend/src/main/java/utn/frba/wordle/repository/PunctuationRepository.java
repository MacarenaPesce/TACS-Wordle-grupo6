package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.entity.PunctuationEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.model.Language;

import java.time.LocalDate;
import java.util.List;

public interface PunctuationRepository extends CrudRepository<PunctuationEntity, Long> {


    @Query(value = "select p.* from Registration r, Punctuation p \n" +
            "where r.id_user = :userId \n" +
            "and r.id = p.id_registration \n" +
            "and p.date = :now", nativeQuery = true)
    List<PunctuationEntity> findTodayResults(Long userId, LocalDate now);

    @Query(value = "select p.* from Punctuation p, Registration r \n" +
            "where r.id = p.id_registration \n" +
            "and r.id_tournament = :idTournament \n", nativeQuery = true)
    List<PunctuationEntity> findResultsFromTournament(Long idTournament);
}
