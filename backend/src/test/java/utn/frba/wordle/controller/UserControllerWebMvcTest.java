package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.service.UserService;
import utn.frba.wordle.utils.TestUtils;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerWebMvcTest {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void iCanFindAllUsers() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/users";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @SneakyThrows
    @Test
    public void iCanFindUsersByTheirNames() {
        Session session = TestUtils.getMockSession();
        String filter = "aFilter";

        String urlController = "/api/users?username=" + filter;
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findByName(filter);
    }
}
