package utn.frba.wordle.repository;

import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.entity.TournamentEntity;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {
}
