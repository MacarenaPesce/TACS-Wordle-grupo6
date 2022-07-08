package utn.frba.wordle.controller;

import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.chat.DictionaryChat;
import utn.frba.wordle.chat.HelpChat;
import utn.frba.wordle.chat.TournamentChat;
import utn.frba.wordle.chat.UserChat;
import utn.frba.wordle.client.TeleSender;

import utn.frba.wordle.model.tele.Update;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
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
    @Autowired
    DictionaryChat dictionaryChat;

    @Autowired
    UserChat userChat;
    @Autowired
    TournamentChat tournamentChat;
    @Autowired
    UserService userService;

    final String start = "Wordle ♟ - Bienvenida\n\n" +
                    "/help - Generar trampas para Wordle\n" +
                    "/definition - Obtener definicion de una palabra en inglés o español\n" +
                    "/submit - Cargar los resultados obtenidos del dia\n" +
                    /*"/create - Crear un torneo nuevo\n" +
                    "/addmember - Agregar un usuario a uno de mis torneos\n" +
                    "/join - Unirme a un torneo publico antes de empezar\n" +
                    "/ranking - Visualizar el ranking de un torneo\n" +
                    "/tournament - Obtener informacion de un torneo\n" +*/
                    "/users - Administrar usuarios\n" +
                    "/tournaments - Administrar torneos";

    final String users = "Wordle ♟ - Usuario\n\n" +
            "/register - Elija su nombre de usuario\n" +
            "/users_list - Lista todos los usuarios existentes\n";

    final String tournaments = "Wordle ♟ - Torneos\n\n" +
            //"/myCreatedTournaments - Ver mis torneos creados (probar despues de create)\n" +
            "/publicTournaments - Ver lista de torneos publicos a punto de comenzar, a los cuales unirme (probar antes de join)\n" +
            "/myTournaments - Ver torneos en los que estoy participando (probar despues de join o create)\n" +
            "/publicStarted - Ver torneos publicos en juego, para poder consultar rankings (hay torneos ya generados con rankings en juego)\n" +
            "/finalizedTournaments - Ver torneos finalizados en los que fui participe\n\n" +
            "/tournament - Obtener informacion de un torneo cualquiera\n" +
            "/ranking - Mostrar todos los participantes de un torneo cualquiera (probar despues de addmember), ordenados por ranking actual\n\n" +
            "/create - Crear un torneo\n" +
            "/addmember - Agregar un usuario a uno de mis torneos\n" +
            "/join - Unirme a un torneo publico pendiente de comenzar";

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
        String humanName = update.getMessage().getFrom().getFirst_name();

        //interpretar mensaje
        if(text.matches("/[^ ](.*)")){  //es comando

            //separar la primera palabra del resto de parametros
            String[] arr = text.substring(1).split(" ", 2);
            String caso = arr[0];
            String params_string;
            if(arr.length > 1){
                params_string = arr[1];
            }else{
                params_string = "";
            }

            //meter los parametros en un array
            String[] params = params_string.split("\\s+");
            //procesar
            processCommand(caso, params, chat_id, true, humanName);

        }else { //no es comando
            if(!casoActual.containsKey(chat_id)){
                // mensaje generico
                String mensajeEnvio = humanName+", su mensaje "+update.getMessage().getMessage_id()+" dice: \n"+text;
                sender.sendMessage(mensajeEnvio, chat_id, "");
            }else{
                // si el usuario se encuentra dentro de un mensaje interactivo
                String[] params = text.split("\\s+");
                processCommand(casoActual.get(chat_id), params, chat_id, false, humanName);
            }
        }

        return new ResponseEntity<>(myString, HttpStatus.OK);
    }

    private void processCommand(String caso, String[] params, Long chat_id, boolean restart, String humanName) throws IOException, URISyntaxException {
        String todo = "\n\ntodo 501";
        switch(caso)
        {
            case "users" :
                sender.sendMessage(users, chat_id, "");
                break;

            case "tournaments" :
                sender.sendMessage(tournaments, chat_id, "");
                break;

            case "help" :
                helpChat.processHelp(params, chat_id, restart, casoActual);
                break;

            case "definition" :
                dictionaryChat.processDictionary(params, chat_id, restart, casoActual);
                break;

            case "checkScores" :
                userChat.processCheckScores(chat_id, humanName);
                break;

            case "submit" :
                userChat.processSubmit(chat_id, humanName);
                //programar mensaje para que el usuario recuerde volver a cargar al dia siguiente. --> no se puede de la api de bots, solo del cliente de telegram. Habria que hacer el schedule manualmente.
                break;

            case "definitionES" :
                sender.sendMessage("Obtener definicion en español"+todo, chat_id, "");
                break;

            case "definitionEN" :
                sender.sendMessage("Obtener definicion en english"+todo, chat_id, "");
                break;

            case "submitES" :
                userChat.processSubmitFull(chat_id, restart, params[0], casoActual, true);
                break;

            case "submitEN" :
                userChat.processSubmitFull(chat_id, restart, params[0], casoActual, false);
                break;

            //-------------------------------------------------------------------

            case "exit" :
                casoActual.remove(chat_id);
                sender.sendMessage(start, chat_id, "");
                break;

            case "start" :
                sender.sendMessage("Buenas tardes/días/noches "+humanName+"! <3", chat_id, "");

                String user = userService.findUsernameByTelegramID(chat_id);
                if(user == null)
                    sender.sendMessage("Usted no se encuentra registrado/a.\n\nHaga /register para poder utilizar torneos", chat_id, "");
                else
                    sender.sendMessage("Usted se encuentra registrado/a para siempre bajo el usuario: "+user, chat_id, "");

                sender.sendMessage(start, chat_id, "");
                break;

            case "mas" :        // para ir a la siguiente pagina de una lista
                String casoGuardado = casoActual.get(chat_id);
                if(casoGuardado == null || !Arrays.asList("users_list", "myCreatedTournaments", "myTournaments", "publicTournaments", "publicStarted", "finalizedTournaments").contains(casoGuardado)){
                    sender.sendMessage("Comando no valido aquí", chat_id, "");
                    return;
                }
                processCommand(casoGuardado, null, chat_id, false, humanName);
                break;

            //----------- users -------------------------------------------------

            case "register" :
                userChat.processRegister(chat_id, restart, params[0], casoActual);
                break;

            case "users_list" :
                userChat.processUsersList(chat_id, restart, casoActual);
                break;

            //----------- tournaments -------------------------------------------

            case "myCreatedTournaments" :
                sender.sendMessage("Ver mis torneos creados\nno existe endpoint"+todo, chat_id, "");
                break;

            case "myTournaments" :
                tournamentChat.processMyTournaments(chat_id, restart, casoActual);
                break;

            case "publicTournaments" :
                tournamentChat.processPublicTournaments(chat_id, restart, casoActual);
                break;

            case "publicStarted" :
                tournamentChat.processPublicStarted(chat_id, restart, casoActual);
                break;

            case "finalizedTournaments" :
                tournamentChat.processFinalizedTournaments(chat_id, restart, casoActual);
                break;

            case "tournament" :
                sender.sendMessage("Obtener informacion de un torneo"+todo, chat_id, "");
                break;

            case "ranking" :
                sender.sendMessage("Visualizar el ranking de un torneo"+todo, chat_id, "");
                break;

            case "create" :
                sender.sendMessage("Crear un torneo"+todo, chat_id, "");
                break;

            case "addmember" :
                sender.sendMessage("Agregar un usuario a uno de mis torneos"+todo, chat_id, "");
                break;

            case "join" :
                sender.sendMessage("Unirme a un torneo publico pendiente de empezar"+todo, chat_id, "");
                break;


            default :
                sender.sendMessage("Comando no reconocido: \n"+caso, chat_id, "");
        }
    }


}
