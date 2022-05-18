package utn.frba.wordle.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utn.frba.wordle.model.dto.LoginDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {

    @Autowired
    UserService userService;

    protected UserDto getUserDto(String s, String usernameTest2) {
        LoginDto user = LoginDto.builder()
                .email(s)
                .username(usernameTest2)
                .build();
        return userService.createUser(user);
    }

}
