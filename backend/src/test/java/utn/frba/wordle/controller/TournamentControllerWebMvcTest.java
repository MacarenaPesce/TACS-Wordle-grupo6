package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import utn.frba.wordle.model.dto.RegistrationDto;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.http.CreateTournamentRequest;
import utn.frba.wordle.model.http.FindTournamentsFilters;
import utn.frba.wordle.model.pojo.Session;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.utils.TestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(TournamentController.class)
public class TournamentControllerWebMvcTest extends AbstractWebMvcTest {

    @MockBean
    private TournamentService tournamentService;

    @SneakyThrows
    @Test
    public void iCanAddNewTournament() {
        CreateTournamentRequest request = CreateTournamentRequest.builder()
                .language(Language.ES)
                .type(TournamentType.PUBLIC)
                .name("name")
                .build();

        TournamentDto serviceDto = TournamentDto.builder()
                .language(request.getLanguage())
                .type(request.getType())
                .name(request.getName())
                .build();

        when(tournamentService.create(any(), any())).thenReturn(serviceDto);

        Session session = TestUtils.getMockSession();
        String urlController = "/api/tournaments/";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).create(serviceDto, session.getUserId());
    }

    @SneakyThrows
    @Test
    public void iCanAddNewMemberToTournament() {
        Long userId = 2L;
        Long tourneyID = 40L;
        Session session = TestUtils.getMockSession();
        RegistrationDto registration = RegistrationDto.builder()
                .punctuations(new ArrayList<>())
                .user(UserEntity.builder().build())
                .build();
        when(tournamentService.addMember(any(), any(), any())).thenReturn(registration);

        String urlController = "/api/tournaments/"+tourneyID+"/members/"+userId;
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).addMember(userId, tourneyID, session.getUserId());
    }

    @SneakyThrows
    @Test
    public void iCanJoinTournament() {
        Session session = TestUtils.getMockSession();
        RegistrationDto registration = RegistrationDto.builder()
                .punctuations(new ArrayList<>())
                .user(UserEntity.builder().build())
                .build();
        when(tournamentService.join(any(), any())).thenReturn(registration);

        Long tournamentId = 123L;
        String urlController = "/api/tournaments/" + tournamentId + "/join";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).join(session.getUserId(), tournamentId);
    }

    @SneakyThrows
    @Test
    public void iCanListPublicTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/public";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).listPublicActiveTournaments(1, 100);
    }

    @SneakyThrows
    @Test
    public void iCanListPublicTournamentsWithPagination() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/public?pageNumber=3&maxResults=7";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).listPublicActiveTournaments(3, 7);
    }

    @SneakyThrows
    @Test
    public void iCanFindPublicTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/public?name=eaea";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findPublicActiveTournaments("eaea", 1, 100);
    }

    @SneakyThrows
    @Test
    public void iCanFindPublicTournamentsWithPagination() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/public?name=eaea&pageNumber=3&maxResults=7";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findPublicActiveTournaments("eaea", 3, 7);
    }

    @SneakyThrows
    @Test
    public void iCanPublishResults() {
        ResultDto request = ResultDto.builder()
                .userId(1L)
                .result(5L)
                .language(Language.ES)
                .build();
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/submitResults";
        mvc.perform(post(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).submitResults(session.getUserId(), request);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheRankingOfATourney() {
        Long idTournament = 22L;
        Session session = TestUtils.getMockSession();

        String urlController = String.format("/api/tournaments/%s/ranking", idTournament);
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getRanking(idTournament, 1, 100);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheRankingOfATourneyWithPagination() {
        Long idTournament = 22L;
        Session session = TestUtils.getMockSession();

        String urlController = String.format("/api/tournaments/%s/ranking?pageNumber=3&maxResults=7", idTournament);
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getRanking(idTournament, 3, 7);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheirScoreFromATourney() {
        Long idTournament = 22L;
        Session session = TestUtils.getMockSession();

        String urlController = String.format("/api/tournaments/%s/ranking/myScore", idTournament);
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getScoreFromUser(idTournament, session.getUsername());
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListOfReadyTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/READY";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findUserTournamentsByStateWithPagination(session.getUserId(), State.READY, 1, 100);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListOfStartedTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/STARTED";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findUserTournamentsByStateWithPagination(session.getUserId(), State.STARTED, 1, 100);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListOfFinishedTournaments() {
        Session session = TestUtils.getMockSession();
        FindTournamentsFilters filters = FindTournamentsFilters.builder()
                .userId(session.getUserId())
                .state(State.FINISHED)
                .maxResults(100)
                .pageNumber(1)
                .build();

        String urlController = "/api/tournaments?state=FINISHED";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findTournamentsGetTotalPages(filters);
        verify(tournamentService).findTournaments(filters);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListOfReadyTournamentsWithPagination() {
        Session session = TestUtils.getMockSession();
        FindTournamentsFilters filters = FindTournamentsFilters.builder()
                .userId(session.getUserId())
                .state(State.READY)
                .maxResults(7)
                .pageNumber(3)
                .build();

        String urlController = "/api/tournaments?state=READY&pageNumber=3&maxResults=7";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findTournamentsGetTotalPages(filters);
        verify(tournamentService).findTournaments(filters);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListTheirTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/myTournaments";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getActiveTournamentsFromUser(session.getUserId(), 1, 100);
    }

    @SneakyThrows
    @Test
    public void aUserCanGetTheListTheirTournamentsWithPagination() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/myTournaments?pageNumber=3&maxResults=7";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).getActiveTournamentsFromUser(session.getUserId(), 3, 7);
    }

    @SneakyThrows
    @Test
    public void aUserCanFilterTheirTournaments() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/myTournaments?name=asdas";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findActiveTournamentsFromUser(session.getUserId(), "asdas", 1, 100);
    }
    @SneakyThrows
    @Test
    public void aUserCanFilterTheirTournamentsWithPagination() {
        Session session = TestUtils.getMockSession();

        String urlController = "/api/tournaments/myTournaments?name=asdas&pageNumber=3&maxResults=7";
        mvc.perform(get(urlController)
                .header(AUTHORIZATION_HEADER_NAME, session.getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tournamentService).findActiveTournamentsFromUser(session.getUserId(), "asdas", 3, 7);
    }
}