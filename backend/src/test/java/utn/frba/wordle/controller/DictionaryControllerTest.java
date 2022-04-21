package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.DictionaryService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DictionaryController.class)
public class DictionaryControllerTest {

    @MockBean
    private DictionaryService dictionaryService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoObtenerLaDefinicionDeUnaPalabra() {

        String palabra = "prueba";
        Language language = Language.ES;
        String urlController = "/api/dictionary/" + language + "/" + palabra;
        mvc.perform(get(urlController)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(dictionaryService).getDefinitions(language, palabra);
    }

}