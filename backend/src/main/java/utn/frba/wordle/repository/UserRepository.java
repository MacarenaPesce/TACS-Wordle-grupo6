package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.model.entity.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM user u WHERE u.username = :username and u.password = :password", nativeQuery = true)
    UserEntity findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT * FROM user u WHERE u.username = :username", nativeQuery = true)
    UserEntity findByUsername(String username);

    @Query(value = "select u.* from user u, registration r \n" +
            "where r.Id_Tournament = :tourneyId \n" +
            "and r.Id_User = u.id", nativeQuery = true)
    List<UserEntity> getTournamentMembers(Long tourneyId);

    @Query(value = "SELECT * FROM user u WHERE u.email = :email", nativeQuery = true)
    UserEntity findByEmail(String email);
}
