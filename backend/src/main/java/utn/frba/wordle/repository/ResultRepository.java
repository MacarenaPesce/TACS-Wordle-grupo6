package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.entity.ResultEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.model.Language;

import java.time.LocalDate;
import java.util.List;

public interface ResultRepository extends CrudRepository<ResultEntity, Long> {


    @Query(value = "select r from ResultEntity r where user = :user and language = :language and date = :now")
    List<ResultEntity> findTodayResults(UserEntity user, Language language, LocalDate now);
}
