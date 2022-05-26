package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.DictionaryService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DictionaryController.class)
public class DictionaryControllerWebMvcTest extends AbstractWebMvcTest {

    @MockBean
    private DictionaryService dictionaryService;

    @SneakyThrows
    @Test
    public void iCanObtainTheDefinitionOfOneWord() {

        String word = "word";
        Language language = Language.ES;
        String urlController = "/api/dictionary/" + language + "/" + word;
        mvc.perform(get(urlController)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(dictionaryService).getDefinitions(language, word);
    }

}