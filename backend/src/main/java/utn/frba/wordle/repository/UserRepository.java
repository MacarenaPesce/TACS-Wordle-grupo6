package utn.frba.wordle.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import utn.frba.wordle.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM user u WHERE u.username = :username and u.password = :password", nativeQuery = true)
    UserEntity findByUsernameAndPassword(String username, String password);
}
