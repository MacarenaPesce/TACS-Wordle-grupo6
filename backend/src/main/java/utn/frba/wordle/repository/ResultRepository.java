package utn.frba.wordle.repository;

import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.entity.ResultEntity;

public interface ResultRepository extends CrudRepository<ResultEntity, Long> {


}
