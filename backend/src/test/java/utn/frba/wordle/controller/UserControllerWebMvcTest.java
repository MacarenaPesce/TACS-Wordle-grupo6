package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.UserService;
import utn.frba.wordle.utils.TestUtils;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerWebMvcTest {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final Class<UserDto> dtoClass = UserDto.class;

    @MockBean
    private UserService userService;

    @MockBean
    PunctuationService punctuationService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoBuscarTodosLosUsuarios() {
        SessionDto sessionDto = TestUtils.getMockSession();

        String urlController = "/api/users";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findAll();
    }

    @SneakyThrows
    @Test
    public void puedoBuscarTodosLosUsuariosFiltrandolosPorNombre() {
        SessionDto sessionDto = TestUtils.getMockSession();
        String filter = "aFilter";

        String urlController = "/api/users?username=" + filter;
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).findByName(filter);
    }
}
