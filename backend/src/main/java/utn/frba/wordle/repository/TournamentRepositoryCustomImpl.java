package utn.frba.wordle.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.http.FindTournamentsFilters;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class TournamentRepositoryCustomImpl implements TournamentRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    CriteriaBuilder cb;
    List<Predicate> predicates;
    Root<TournamentEntity> entityRoot;

    @Override
    public List<TournamentEntity> findTournaments(FindTournamentsFilters params){
        //Integer offset = (params.getPageNumber() - 1) * params.getMaxResults();

        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TournamentEntity> query = cb.createQuery(TournamentEntity.class);
        entityRoot = query.from(TournamentEntity.class);

        predicates = new ArrayList<>();

        addPredicateCaseInsensitive(params.getName());
        addPredicate(params.getType());
        addPredicate(params.getState());

        query.select(entityRoot).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    private void addPredicate(TournamentType pattern) {
        if(pattern != null){
            Path<String> namePath = entityRoot.get("type");
            predicates.add(cb.equal(namePath, pattern));
        }
    }

    private void addPredicate(State statePattern) {
        if(statePattern == null) {
            return;
        }
        Predicate date;
        switch (statePattern){
            case READY:
                date = cb.greaterThan(entityRoot.get("start"), new Date());
                predicates.add(date);
                break;
            case FINISHED:
                date = cb.lessThan(entityRoot.get("finish"), new Date());
                predicates.add(date);
                break;
            case STARTED:
                date = cb.and(
                        cb.lessThan(entityRoot.get("start"), new Date()),
                        cb.greaterThan(entityRoot.get("finish"), new Date())
                );
                predicates.add(date);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + statePattern);
        }
    }

    private void addPredicateCaseInsensitive(String pattern) {
        Path<String> namePath = entityRoot.get("name");
        if (pattern != null) {
            predicates.add(cb.like(cb.lower(namePath), "%"+pattern+"%"));
        }
    }
}
