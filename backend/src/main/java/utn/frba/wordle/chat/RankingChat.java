package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.entity.PunctuationEntity;
import utn.frba.wordle.model.entity.RankingEntity;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.pojo.Punctuation;
import utn.frba.wordle.service.TournamentService;

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
                sender.sendMessage("Ingresa el id de torneo ", chat_id, "");
                pasoActual.put(chat_id, 2);
                /*
                String user = userService.findUsernameByTelegramID(chat_id);
                if(user == null){
                    pasoActual.put(chat_id, 2);
                    sender.sendMessage(usuario, chat_id, "");
                }else {
                    casoActual.remove(chat_id, "register");
                    sender.sendMessage("Usted ya se encuentra registrado bajo el usuario: "+user, chat_id, "");
                }*/
                break;

            case 2 :    //buscar ranking
                //verificar que se recibió un id
                if(!message.matches("\\d+")){
                    sender.sendMessage("Envie algo que tenga reminiscencia a un id", chat_id, "");
                    return;
                }

                //verificar que exista el torneo //todo copiar de get info
                if(message.matches("\\w+")) {
                    //verificar que el username no este en el hashmap temporal de otro usuario
                    //UserEntity userEntity = userService.getUserByUsername(message.toLowerCase());
                }

                //verificar si es privado, que sea miembro del torneo //todo copiar de get info

                List<Punctuation> ranking = tournamentService.getRanking(Long.parseLong(message),1,100);

                String rankingString = ranking.stream().map(Punctuation::toStringTelegram).collect(Collectors.joining("\n\n"));


                sender.sendMessage(rankingString, chat_id, "");
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }
    }

}
