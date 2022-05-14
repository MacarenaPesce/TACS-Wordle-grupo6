package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.dto.PositionsResponseDto;
import utn.frba.wordle.service.UserService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.RANDOM;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoObtenerElListadoDePosicionesDeMisTorneos() {
        PositionsResponseDto request = RANDOM.nextObject(PositionsResponseDto.class);

        String urlController = "/api/users/getPositions";
        mvc.perform(get(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(userService).getPositions();
    }
}