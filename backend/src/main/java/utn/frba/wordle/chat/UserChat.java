package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.TournamentService;
import utn.frba.wordle.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserChat {
    //no existe login, el userid de telegram me asegura que solo una persona puede acceder
    //el usuario debe registrarse la primera vez eligiendo nombre de usuario, y este se guarda en la base de datos

    //opcion de crear nueva contraseña, en caso de olvido, para poder usar el mismo usuario en el frontend  //todo

    //opcion para cambiar nombre de usuario (opcional)
    //al momento del primer registro agregar forma de reutilizar un usuario que ya existia en el frontend (opcional)

    @Autowired
    TeleSender sender;
    @Autowired
    UserService userService;
    @Autowired
    PunctuationService punctuationService;
    @Autowired
    TournamentService tournamentService;
    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    final HashMap<Long, String> username = new HashMap<>();
    final HashMap<Long, String> password = new HashMap<>();

    public void processUsersList(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "users_list");
            pasoActual.put(chat_id, 1);
        }

        Integer pag = pasoActual.get(chat_id);
        List<UserDto> users = userService.getAllWithPagination(25, pag);
        String usersString = users.stream().map(UserDto::toStringTelegram).collect(Collectors.joining("\n"));

        String mas = "\n\n/mas para mostrar más resultados";
        String exit = "\n\n/exit para salir";

        if(users.isEmpty()){
            casoActual.remove(chat_id, "users_list");
            sender.sendMessage("No hay mas resultados", chat_id, "");
        }else {
            pasoActual.put(chat_id, pag+1);
            sender.sendMessage("Lista de usuarios - Página "+pag+"\n\n"+usersString+mas+exit, chat_id, "");
        }

    }

    public void processRegister(Long chat_id, boolean restart, String message, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "register");
            pasoActual.put(chat_id, 1);
        }

        String exit = "\n\nEscriba /exit para volver a comenzar";
        String usuario = "Indique su nombre de usuario deseado:"; // [A-Za-z0-9_] equals \w
        String contrasenia = "indique su nueva contraseña (para iniciar sesion desde la pagina web): ";
        String email = "Indique su email: ";

        Integer step = pasoActual.get(chat_id);
        switch (step){
            case 1 :    //verificar no registro y pedir nuevo nombre de usuario
                String user = userService.findUsernameByTelegramID(chat_id);
                if(user == null){
                    pasoActual.put(chat_id, 2);
                    sender.sendMessage(usuario, chat_id, "");
                }else {
                    casoActual.remove(chat_id, "register");
                    sender.sendMessage("Usted ya se encuentra registrado bajo el usuario: "+user, chat_id, "");
                }
                break;

            case 2 :    //verificar nombre de usuario y pedir contraseña
                if(message.matches("\\w+")){
                    //todo verificar que el username no este en el hashmap temporal de otro usuario
                    UserEntity userEntity = userService.getUserByUsername(message.toLowerCase());
                    if(userEntity != null){
                        sender.sendMessage("El usuario "+message+" ya esta en uso, elija otro."+exit, chat_id, "");
                        return;
                    }

                    username.put(chat_id, message);
                    pasoActual.put(chat_id, 3);
                    sender.sendMessage(contrasenia, chat_id, "");
                }else{
                    sender.sendMessage(usuario+exit, chat_id, "");
                }
                break;

            case 3:     //verificar contraseña y pedir email
                //todo hacer alguna verificacion regex
                password.put(chat_id, message);
                pasoActual.put(chat_id, 4);
                sender.sendMessage(email, chat_id, "");
                break;

            case 4:     //verificar email y registrar usuario
                //todo hacer alguna verificacion regex
                UserEntity userEntity = userService.findUserByEmail(message.toLowerCase());
                if(userEntity != null){
                    sender.sendMessage("El email ya esta en uso, elija otro."+exit, chat_id, "");
                    return;
                }

                //crear el usuario
                //todo asegurar que nadie mas haya tomado mi usuario reservado previamente
                String name = username.get(chat_id);
                userService.createUserTelegram(name, password.get(chat_id), message, chat_id);

                sender.sendMessage("Ahora eres para siempre: "+name, chat_id, "");

                username.remove(chat_id);
                password.remove(chat_id);
                casoActual.remove(chat_id, "register");
                break;


            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }

    }

    public void processCheckScores(Long chat_id, String humanName) throws IOException, URISyntaxException {

        Long userid = userService.findUseridByTelegramID(chat_id);

        Long spanishScore = punctuationService.getTodaysResult(userid, Language.ES);
        Long englishScore = punctuationService.getTodaysResult(userid, Language.EN);
        String spanish;
        String english;
        if(spanishScore == 0)
            spanish = "_sin cargar_ - /submitES";
        else
            spanish = spanishScore.toString();
        if(englishScore == 0)
            english = "_sin cargar_ - /submitEN";
        else
            english = englishScore.toString();

        sender.sendMessage("_Puntajes del día para "+humanName+"_: \n\n*Español*: "+spanish+"\n\n*English*: "+english+"\n\n*Importante*: recuerde jugar todos los dias a Wordle y cargar aquí sus resultados. Viva para nosotros!", chat_id, "Markdown");
    }

    public void processSubmit(Long chat_id, String humanName) throws IOException, URISyntaxException {

        sender.sendMessage("[Jugar a Wordle en Español](https://wordle.danielfrg.com/)", chat_id, "Markdown");
        sender.sendMessage("[Play Wordle in English](https://www.nytimes.com/games/wordle/index.html)", chat_id, "Markdown");
        processCheckScores(chat_id,humanName);
        //sender.sendMessage("Cargar los resultados del día: \n\n/submitES - Español\n\n/submitEN - English", chat_id, "");
    }

    public void processSubmitFull(Long chat_id, boolean restart, String message, HashMap<Long, String> casoActual, boolean spanish) throws IOException, URISyntaxException {
        if(restart){
            if(spanish)
                casoActual.put(chat_id, "submitES");
            else
                casoActual.put(chat_id, "submitEN");
            pasoActual.put(chat_id, 1);
        }

        Language lang;
        if(spanish)
            lang = Language.ES;
        else
            lang = Language.EN;

        Long userid = userService.findUseridByTelegramID(chat_id);

        Long score = punctuationService.getTodaysResult(userid, lang);
        if(score != 0){
            sender.sendMessage("Ya cargó hoy, espere hasta mañana, no sea ansioso", chat_id, "");
            return;
        }

        Integer step = pasoActual.get(chat_id);
        switch (step){
            case 1 :
                pasoActual.put(chat_id, 2);
                sender.sendMessage("Indique el puntaje obtenido del 1 al 7 (sin mentir ni equivocarse sin querer)", chat_id, "");
                break;

            case 2 :
                Long new_score;
                try {
                    new_score = Long.parseLong(message);
                }catch (NumberFormatException e){
                    new_score = 99L;
                }
                if(new_score > 7 || new_score < 1){
                    sender.sendMessage("Intente con un puntaje válido por favor", chat_id, "");
                    return;
                }

                ResultDto dto = ResultDto.builder()
                        .language(lang)
                        .result(new_score)
                        .userId(userid)
                        .build();
                try {
                    tournamentService.submitResults(userid, dto);
                }catch (BusinessException e){
                    sender.sendMessage("Alguien estuvo manipulando mis resultados desde que inicie el comando", chat_id, "");
                } finally {
                    if(spanish)
                        casoActual.remove(chat_id, "submitES");
                    else
                        casoActual.remove(chat_id, "submitEN");
                }
                sender.sendMessage("Exitos!!!! No olvide cargar también el otro idioma y volver mañana\n\n/checkScores - revisar mis puntajes del dia de la fecha", chat_id, "");
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }

    }
}
