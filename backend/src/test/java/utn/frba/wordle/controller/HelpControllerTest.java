package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.service.HelpService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.RANDOM;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(HelpController.class)
public class HelpControllerTest {

    private final Class<HelpRequestDto> dtoClass = HelpRequestDto.class;

    @MockBean
    private HelpService helpService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoObtenerAyudarAPartirDeUnaSerieDeLetras() {
        HelpRequestDto request = RANDOM.nextObject(dtoClass);

        String lenguaje = "ES";
        String urlController = "/api/help/" + lenguaje;
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(helpService).solution(request, lenguaje);
    }

}