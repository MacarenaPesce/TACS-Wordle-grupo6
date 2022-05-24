package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.utils.TestUtils;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PunctuationController.class)
public class PunctuationControllerWebMvcTest {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    @MockBean
    PunctuationService punctuationService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void iCanCreateNewTournament() {

        Language language = Language.ES;
        Session session = TestUtils.getMockSession();
        String urlController = String.format("/api/punctuation/todaysResult/%s", language);
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(punctuationService).getTodaysResult(session.getUserId(), language);
    }
}