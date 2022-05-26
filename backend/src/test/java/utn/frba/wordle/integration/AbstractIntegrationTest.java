package utn.frba.wordle.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utn.frba.wordle.logging.WordleLogger;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected UserService userService;

    @BeforeAll
    public static void setUp(){
        WordleLogger.enableDebug(true);
    }

    protected UserDto getUserDto(String email, String username) {
        return userService.createUser(username, "123asd", email);
    }
}
