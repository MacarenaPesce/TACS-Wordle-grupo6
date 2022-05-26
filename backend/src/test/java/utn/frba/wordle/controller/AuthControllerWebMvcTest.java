package utn.frba.wordle.controller;

import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.http.LoginRequest;
import utn.frba.wordle.model.http.RegisterRequest;
import utn.frba.wordle.service.AuthService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(AuthController.class)
public class AuthControllerWebMvcTest extends AbstractWebMvcTest {

    @MockBean
    private AuthService authService;

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

    @SneakyThrows
    @Test()
    public void ifICantRegisterIGetAnErrorMessage() {
        String username = "qwe";
        String password = "asd";
        String email = "nas@nasd.com";
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        when(authService.register(request.getUsername(), request.getPassword(), request.getEmail())).thenThrow(new BusinessException("User AlreadyRegistered"));
        String expectedResponse = "User AlreadyRegistered";

        String urlController = "/api/auth/register";
        MvcResult result = mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.isTrue(content.contains(expectedResponse));
    }

    @SneakyThrows
    @Test()
    public void ifICantLoginIGetAnErrorMessage() {
        String username = "qwe";
        String password = "asd";
        String email = "nas@nasd.com";
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
        when(authService.login(username, password)).thenThrow(new BusinessException("User not valid"));
        String expectedResponse = "User not valid";

        String urlController = "/api/auth/login";
        MvcResult result = mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.isTrue(content.contains(expectedResponse));
    }
}
