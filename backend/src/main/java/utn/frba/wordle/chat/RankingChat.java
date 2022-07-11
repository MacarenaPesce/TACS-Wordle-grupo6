package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.RankingEntity;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.enums.TournamentType;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingChat {

    @Autowired
    TeleSender sender;

    @Autowired
    TournamentService tournamentService;

    @Autowired
    UserService userService;

    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    final HashMap<Long, StringBuilder> commandBuilder = new HashMap<>();

    public void processRanking(Long chat_id, String message, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "ranking");
            pasoActual.put(chat_id, 1);
        }

        String exit = "\n\nEscriba /exit para volver a comenzar";
        String torneo = "Indique el id del torneo deseado:"; // [0-9]+ equals \w

        Integer step = pasoActual.get(chat_id);
        switch (step){
            case 1 :
                sender.sendMessageWithKB("Ingresa el id de torneo ", chat_id, "", "[[\"/publicStarted\"]]");
                pasoActual.put(chat_id, 2);

                break;

            case 2 :    //buscar ranking previas validaciones
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

                //obtener ranking
                List<Punctuation> ranking = tournamentService.getRanking(Long.parseLong(message),1,100);

                //parsear ranking en string
                String rankingString = ranking.stream().map(Punctuation::toStringTelegram).collect(Collectors.joining("\n\n"));

                sender.sendMessageDelKB(rankingString, chat_id, "");
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }
    }

}
