package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.ResultDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.utils.TestUtils;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.RANDOM;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(TournamentsController.class)
public class TournamentsControllerTest {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final Class<TournamentDto> dtoClass = TournamentDto.class;

    @MockBean
    private TournamentService tournamentService;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void puedoCrearUnNuevoTorneo() {
        TournamentDto request = RANDOM.nextObject(dtoClass);
        request.setFinish(null);
        request.setStart(null);

        SessionDto sessionDto = TestUtils.getValidSession();
        String urlController = "/api/tournaments/";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).create(request, sessionDto.getUserId());
    }

    @SneakyThrows
    @Test
    public void puedoAgregarUnMiembroAUnTorneo() {
        MemberDto request = RANDOM.nextObject(MemberDto.class);
        SessionDto sessionDto = TestUtils.getValidSession();

        String urlController = "/api/tournaments/addmember";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).addMember(request, sessionDto.getUserId());
    }

    @SneakyThrows
    @Test
    public void puedoUnirmeAUnTorneo() {
        MemberDto request = RANDOM.nextObject(MemberDto.class);

        int id = 123;
        String urlController = "/api/tournaments/" + id + "/join";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).join(id);
    }

    @SneakyThrows
    @Test
    public void puedoListarLosTorneosPublicos() {

        String urlController = "/api/tournaments/public";
        mvc.perform(get(urlController)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).listPublicTournaments();
    }

    @SneakyThrows
    @Test
    public void puedoPublicarLosResultados() {
        ResultDto request = RANDOM.nextObject(ResultDto.class);

        String urlController = "/api/tournaments/submitResults";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).submitResults(request);
    }
}