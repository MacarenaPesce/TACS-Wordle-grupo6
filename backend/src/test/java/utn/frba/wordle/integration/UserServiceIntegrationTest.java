package utn.frba.wordle.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.service.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    public void aUserCanFindAllUsers(){
        UserDto user1 = getUserDto("jorge@mail.com", "jorge");
        UserDto user2 = getUserDto("jose@mail.com", "jose");
        UserDto user3 = getUserDto("pedro@mail.com", "pedro");

        List<UserDto> users = userService.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    public void aUserCanFindAllUsersUsingFilter(){
        UserDto user1 = getUserDto("jorge@mail.com", "jorge");
        UserDto user2 = getUserDto("jose@mail.com", "jose");
        getUserDto("pedro@mail.com", "pedro");
        String filter = "JO";

        List<UserDto> users = userService.findByName(filter);

        assertThat(users).isNotEmpty();
        assertThat(users).containsExactlyInAnyOrder(user1, user2);
    }


    @Test
    public void aUserCanFindAnotherUserUsingFilterWithPagination() {
        UserDto user1 = getUserDto("jorge@mail.com", "jorge");
        getUserDto("jose@mail.com", "jose");
        UserDto user3 = getUserDto("joaco@mail.com", "joaco");
        getUserDto("majo@mail.com", "majo");
        String filter = "JO";
        Integer maxResults = 2;
        Integer actualPage = 1;

        List<UserDto> users = userService.findByNameWithPagination(filter, maxResults, actualPage);

        assertThat(users).isNotEmpty();
        assertThat(users).containsExactly(user3, user1);
    }
}

