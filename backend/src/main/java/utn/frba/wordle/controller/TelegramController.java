package utn.frba.wordle.controller;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.chat.HelpChat;
import utn.frba.wordle.client.TeleSender;

import utn.frba.wordle.model.tele.Update;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;


@RestController
@RequestMapping("/api/telegram")
@CrossOrigin
public class TelegramController {
    //usar esta dependencia en el caso de necesitar todos los objetos ya modelados:
    //https://github.com/pengrad/java-telegram-bot-api/tree/master/library/src/main/java/com/pengrad/telegrambot/model


    @Autowired
    TeleSender sender;
    @Autowired
    HelpChat helpChat;

    final String start = "Wordle ♟\n\n" +
                    "/register - Elija su nombre de usuario\n" +
                    "/help - Generar trampas para Wordle\n" +
                    "/definition - Obtener definicion de una palabra en inglés o español\n" +
                    "/submit - Cargar los resultados obtenidos del dia\n" +
                    "/create - Crear un torneo nuevo\n" +
                    "/addmember - Agregar un usuario a uno de mis torneos\n" +
                    "/join - Unirme a un torneo publico antes de empezar\n" +
                    "/ranking - Visualizar el ranking de un torneo\n" +
                    "/tournaments - Obtener listas de torneos existentes\n" +
                    "/tournament - Obtener informacion de un torneo";

    /**
     * Caso de uso en el que se encuentra el usuario.
     */
    final HashMap<Long, String> casoActual = new HashMap<>(); //todo debe tener algun tipo de proteccion contra concurrencia? esta cache en forma de array

    @PostMapping("/")
    public ResponseEntity<String> postUpdate(@RequestBody Update update) throws IOException, URISyntaxException {

        String myString = new GsonBuilder().setPrettyPrinting().create().toJson(update);
        System.out.println("Update recibido: \n"+myString);

        String text = update.getMessage().getText();
        Long chat_id = update.getMessage().getChat().getId();

        //interpretar mensaje
        if(text.matches("/[^ ](.*)")){  //es comando

            String[] arr = text.substring(1).split(" ", 2);
            String caso = arr[0];   //help
            String params_string;
            if(arr.length > 1){
                params_string = arr[1];    //es amarillas grises _a_a_
            }else{
                params_string = "";
            }

            String[] params = params_string.split("\\s+");
            processCommand(caso, params, chat_id, true);

        }else { //no es comando
            if(!casoActual.containsKey(chat_id)){
                String mensajeEnvio = update.getMessage().getFrom().getFirst_name()+", su mensaje "+update.getMessage().getMessage_id()+" dice: \n"+text;
                sender.sendMessage(mensajeEnvio, chat_id);
            }else{  // si el usuario se encuentra dentro de un mensaje interactivo
                String[] params = text.split("\\s+");
                processCommand(casoActual.get(chat_id), params, chat_id, false);
            }
        }

        return new ResponseEntity<>(myString, HttpStatus.OK);
    }

    private void processCommand(String caso, String[] params, Long chat_id, boolean restart) throws IOException, URISyntaxException {
        switch(caso)
        {
            case "help" :
                helpChat.processHelp(params, chat_id, restart, casoActual);
                break;

            case "definition" :
                sender.sendMessage("Obtener definicion de una palabra", chat_id);
                break;

            case "exit" :
                casoActual.remove(chat_id);
                sender.sendMessage(start, chat_id);
                break;

            case "submit" :
                sender.sendMessage("Cargar los resultados del dia", chat_id);
                break;

            case "create" :
                sender.sendMessage("Crear un torneo", chat_id);
                break;

            case "start" :
                sender.sendMessage(start, chat_id);
                break;

            case "addmember" :
                sender.sendMessage("Agregar un usuario a uno de mis torneos", chat_id);
                break;

            case "join" :
                sender.sendMessage("Unirme a un torneo publico pendiente de empezar", chat_id);
                break;

            case "ranking" :
                sender.sendMessage("Visualizar el ranking de un torneo", chat_id);
                break;

            case "tournaments" :
                sender.sendMessage("Obtener listas de torneos existentes", chat_id);
                break;

            case "tournament" :
                sender.sendMessage("Obtener informacion de un torneo", chat_id);
                break;

            case "register" :
                sender.sendMessage("Elija su nombre de usuario", chat_id);
                break;

            default :
                sender.sendMessage("Comando no reconocido: \n"+params[0], chat_id);
        }
    }


}
