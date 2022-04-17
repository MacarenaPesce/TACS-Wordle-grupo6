package utn.frba.wordle.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import utn.frba.wordle.dto.MemberDto;
import utn.frba.wordle.dto.TournamentDto;
import utn.frba.wordle.service.TournamentService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utn.frba.wordle.utils.TestUtils.RANDOM;
import static utn.frba.wordle.utils.TestUtils.toJson;

@WebMvcTest(TournamentsController.class)
public class TournamentsControllerTest {

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

        String urlController = "/api/tournaments/";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).create(request);
    }

    @SneakyThrows
    @Test
    public void puedoAgregarUnMiembroAUnTorneo() {
        MemberDto request = RANDOM.nextObject(MemberDto.class);

        String urlController = "/api/tournaments/addmember";
        mvc.perform(post(urlController)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk());

        verify(tournamentService).addMember(request);
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
}