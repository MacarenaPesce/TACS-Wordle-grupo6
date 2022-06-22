package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM wordle.user u WHERE u.username = :username and u.password = :password", nativeQuery = true)
    UserEntity findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM wordle.user u WHERE u.username = :username", nativeQuery = true)
    UserEntity getByUsername(String username);

    @Query(value = "select u.* from wordle.user u, wordle.registration r \n" +
            "where r.Id_Tournament = :tourneyId \n" +
            "and r.Id_User = u.id", nativeQuery = true)
    List<UserEntity> getTournamentMembers(Long tourneyId);

    @Query(value = "SELECT * FROM wordle.user u WHERE u.email = :email", nativeQuery = true)
    UserEntity findByEmail(String email);

    @Query(value = "SELECT * FROM wordle.user u WHERE u.username like %:username%", nativeQuery = true)
    List<UserEntity> findByPartialUsername(String username);

    @Query(value = "SELECT * FROM wordle.user u WHERE u.username like %:username% order by username asc LIMIT :offset,:maxResults", nativeQuery = true)
    List<UserEntity> findByPartialUsernameWithPagination(String username, Integer offset, Integer maxResults);

    @Query(value = "SELECT count(*) FROM wordle.user u WHERE u.username like %:username%", nativeQuery = true)
    Integer findByNameTotalPages(String username);

    @Query(value = "SELECT * FROM wordle.user u WHERE u.username = :username", nativeQuery = true)
    UserEntity findByUsername(String username);
}
