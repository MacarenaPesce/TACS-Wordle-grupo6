package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.utils.TestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        when(tournamentService.create(any(), any())).thenReturn(request);

        SessionDto sessionDto = TestUtils.getMockSession();
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
        Long userId = 2L;
        Long tourneyID = 40L;
        SessionDto sessionDto = TestUtils.getMockSession();
        RegistrationDto registration = RegistrationDto.builder()
                .punctuations(new ArrayList<>())
                .user(UserEntity.builder().build())
                .build();
        when(tournamentService.addMember(any(), any(), any())).thenReturn(registration);

        String urlController = "/api/tournaments/"+tourneyID+"/members/"+userId;
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).addMember(userId, tourneyID, sessionDto.getUserId());
    }

    @SneakyThrows
    @Test
    public void puedoUnirmeAUnTorneo() {
        SessionDto sessionDto = TestUtils.getMockSession();
        RegistrationDto registration = RegistrationDto.builder()
                .punctuations(new ArrayList<>())
                .user(UserEntity.builder().build())
                .build();
        when(tournamentService.join(any(), any())).thenReturn(registration);

        Long tournamentId = 123L;
        String urlController = "/api/tournaments/" + tournamentId + "/join";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).join(sessionDto.getUserId(), tournamentId);
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
        SessionDto sessionDto = TestUtils.getMockSession();

        String urlController = "/api/tournaments/submitResults";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).submitResults(sessionDto.getUserId(), request);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheRankingOfATourney() {
        Long idTournament = 22L;
        SessionDto sessionDto = TestUtils.getMockSession();

        String urlController = String.format("/api/tournaments/%s/ranking", idTournament);
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).orderedPunctuations(idTournament);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListOfReadyTournaments() {
        SessionDto sessionDto = TestUtils.getMockSession();

        String urlController = "/api/tournaments/READY";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findUserTournamentsByState(sessionDto.getUserId(), State.READY);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListTheirTournaments() {
        SessionDto sessionDto = TestUtils.getMockSession();

        String urlController = "/api/tournaments/myTournaments";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, sessionDto.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getTournamentsFromUser(sessionDto.getUserId());
    }
}