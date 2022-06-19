package utn.frba.wordle.repository;

import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.http.FindTournamentsFilters;

import java.util.List;

public interface TournamentRepositoryCustom {
    List<TournamentEntity> findTournaments(FindTournamentsFilters query);
}
