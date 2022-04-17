package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.service.AuthService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.RANDOM;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private final Class<LoginDto> dtoClass = LoginDto.class;

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoLoguearmeCorrectamente() {
        LoginDto request = RANDOM.nextObject(dtoClass);

        String urlController = "/api/auth/login";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());
        verify(authService).login(request);
    }
}
