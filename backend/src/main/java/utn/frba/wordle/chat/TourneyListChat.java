package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.enums.State;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.http.FindTournamentsFilters;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourneyListChat {
    @Autowired
    TeleSender sender;

    @Autowired
    UserService userService;

    @Autowired
    TournamentService tournamentService;

    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    private void processTourneyList(Long chat_id, HashMap<Long, String> casoActual, String command, List<TournamentDto> tourneys, String title, Integer pag, String KB) throws IOException, URISyntaxException {

        String tourneysString = tourneys.stream().map(TournamentDto::toStringTelegramList).collect(Collectors.joining("\n\n"));

        String mas = "\n\n/mas para mostrar más resultados";
        String exit = "\n\n/exit para salir";

        if (tourneys.isEmpty()) {
            casoActual.remove(chat_id, command);
            sender.sendMessageDelKB("No hay mas resultados", chat_id, "");
        } else {
            pasoActual.put(chat_id, pag + 1);
            sender.sendMessageWithKB(title + " - Página " + pag + "\n\n" + tourneysString + mas + exit, chat_id, "", "["+KB+"]");
        }
    }

    public void processMyCreatedTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "myCreatedTournaments";
        if (restart) {
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Long userid = userService.findUseridByTelegramID(chat_id);
        Integer pag = pasoActual.get(chat_id);
        //List<TournamentDto> tourneys = tournamentService.

        //processTourneyList(chat_id, casoActual, command, tourneys, "Mis torneos creados y activos", pag);

    }

    public void processMyTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "myTournaments";
        if (restart) {
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Long userid = userService.findUseridByTelegramID(chat_id);
        Integer pag = pasoActual.get(chat_id);
        List<TournamentDto> tourneys = tournamentService.getActiveTournamentsFromUser(userid, pag, 10);

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos participando", pag, "");
    }

    public void processPublicTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "publicTournaments";
        if (restart) {
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Integer pag = pasoActual.get(chat_id);

        FindTournamentsFilters findTournamentsFilters = FindTournamentsFilters.builder()
                .type(TournamentType.PUBLIC)
                .state(State.READY)
                .pageNumber(pag)
                .maxResults(10)
                .build();
        List<TournamentDto> tourneys = tournamentService.findTournaments(findTournamentsFilters);

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos públicos por comenzar", pag, "");
    }

    public void processPublicStarted(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "publicStarted";
        if (restart) {
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Integer pag = pasoActual.get(chat_id);

        FindTournamentsFilters findTournamentsFilters = FindTournamentsFilters.builder()
                .type(TournamentType.PUBLIC)
                .state(State.STARTED)
                .pageNumber(pag)
                .maxResults(10)
                .build();
        List<TournamentDto> tourneys = tournamentService.findTournaments(findTournamentsFilters);

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos públicos en juego", pag, "[\"/info\",\"/ranking\"]");
    }

    public void processFinalizedTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "finalizedTournaments";
        if (restart) {
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Integer pag = pasoActual.get(chat_id);
        String username = userService.findUsernameByTelegramID(chat_id);

        FindTournamentsFilters findTournamentsFilters = FindTournamentsFilters.builder()
                .name(username)
                .state(State.FINISHED)
                .pageNumber(pag)
                .maxResults(10)
                .build();
        List<TournamentDto> tourneys = tournamentService.findTournaments(findTournamentsFilters);

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos finalizados en los que estuve registrado", pag, "");
    }
}
