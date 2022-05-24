package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.http.LoginRequest;
import utn.frba.wordle.model.http.RegisterRequest;
import utn.frba.wordle.service.AuthService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(AuthController.class)
public class AuthControllerWebMvcTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void iCanLoginCorrectly() {
        String username = "qwe";
        String password = "asd";
        LoginRequest request = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        String urlController = "/api/auth/login";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());
        verify(authService).login(username, password);
    }

    @SneakyThrows
    @Test
    public void iCanRegisterCorrectly() {
        String username = "qwe";
        String password = "asd";
        String email = "nas@nasd.com";
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        String urlController = "/api/auth/register";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated());
        verify(authService).register(request.getUsername(), request.getPassword(), request.getEmail());
    }
}
