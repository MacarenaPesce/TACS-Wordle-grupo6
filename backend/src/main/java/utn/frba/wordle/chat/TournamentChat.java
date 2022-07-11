package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.entity.TournamentEntity;
import utn.frba.wordle.model.entity.UserEntity;
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

import static utn.frba.wordle.model.enums.ErrorMessages.TOURNAMENT_WITH_SAME_NAME_EXISTS;

@Service
public class TournamentChat {
    //cargar mis resultados del dia, y poder revisar si ya habia cargado
    //poder crear un torneo
    //poder unir usuarios a mis torneos
    //poder consultar lista de mis torneos creados
    //poder consultar lista de los torneos a los que estoy unido
    //poder consultar lista de torneos publicos a los que puedo unirme
    //poder unirme a un torneo publico
    //poder consultar lista torneos publicos empezados
    //poder obtener informacion de un torneo
    //poder consultar el ranking de un torneo started o finalized
    //poder consultar lista de torneos finalizados

    @Autowired
    TeleSender sender;

    @Autowired
    UserService userService;
    @Autowired
    TournamentService tournamentService;
    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    private void processTourneyList(Long chat_id, HashMap<Long, String> casoActual, String command, List<TournamentDto> tourneys, String title, Integer pag) throws IOException, URISyntaxException {

        String tourneysString = tourneys.stream().map(TournamentDto::toStringTelegramList).collect(Collectors.joining("\n\n"));

        String mas = "\n\n/mas para mostrar más resultados";
        String exit = "\n\n/exit para salir";

        if(tourneys.isEmpty()){
            casoActual.remove(chat_id, command);
            sender.sendMessage("No hay mas resultados", chat_id, "");
        }else {
            pasoActual.put(chat_id, pag+1);
            sender.sendMessage(title+" - Página "+pag+"\n\n"+tourneysString+mas+exit, chat_id, "");
        }
    }

    public void processMyCreatedTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "myCreatedTournaments";
        if(restart){
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
        if(restart){
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Long userid = userService.findUseridByTelegramID(chat_id);
        Integer pag = pasoActual.get(chat_id);
        List<TournamentDto> tourneys = tournamentService.getActiveTournamentsFromUser(userid, pag, 10);

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos participando", pag);
    }

    public void processPublicTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "publicTournaments";
        if(restart){
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

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos públicos por comenzar", pag);
    }

    public void processPublicStarted(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "publicStarted";
        if(restart){
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

        processTourneyList(chat_id, casoActual, command, tourneys, "Torneos públicos en juego", pag);
    }

    public void processFinalizedTournaments(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String command = "finalizedTournaments";
        if(restart){
            casoActual.put(chat_id, command);
            pasoActual.put(chat_id, 1);
        }

        Integer pag = pasoActual.get(chat_id);

        FindTournamentsFilters findTournamentsFilters = FindTournamentsFilters.builder()
                .state(State.FINISHED)
                .pageNumber(pag)
                .maxResults(10)
                .build();
        List<TournamentDto> tourneys = tournamentService.findTournaments(findTournamentsFilters);

        processTourneyList(chat_id, casoActual, command, tourneys, "Todos los torneos finalizados", pag);
    }

    public void processInfo(Long chat_id, String message, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "tournament");
            pasoActual.put(chat_id, 1);
        }

        Integer step = pasoActual.get(chat_id);
        switch (step){
            case 1 :
                pasoActual.put(chat_id, 2);
                sender.sendMessage("Inserte el id del torneo a consultar info detallada", chat_id, "");
                break;

            case 2 :
                //verificar que se recibió un id
                if(!message.matches("\\d+")){
                    sender.sendMessage("Envie algo que tenga reminiscencia a un id", chat_id, "");
                    return;
                }

                //verificar que exista el torneo
                Long tourneyid = Long.parseLong(message);
                TournamentDto tourney;
                try {
                    tourney = tournamentService.getActiveTournamentById(tourneyid);
                }catch (BusinessException e){
                    sender.sendMessage("No existe torneo activo con ese id, elija otro id", chat_id, "");
                    return;
                }

                //verificar si es privado, que sea miembro del torneo
                if(tourney.getType() == TournamentType.PRIVATE){
                    Long userid = userService.findUseridByTelegramID(chat_id);
                    if(!userService.memberOfTournament(userid, tourneyid)){
                        sender.sendMessage("Usted no forma parte del torneo privado "+tourneyid+", elija otro id", chat_id, "");
                        return;
                    }
                }

                //crear string con la info del torneo y enviar
                String info = tourney.toStringInfoMarkdownV1();

                sender.sendMessage(info, chat_id, "Markdown");
                casoActual.remove(chat_id, "tournament");
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }
    }

}
