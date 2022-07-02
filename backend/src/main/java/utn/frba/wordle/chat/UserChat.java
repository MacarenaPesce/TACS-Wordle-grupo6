package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.service.UserService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static utn.frba.wordle.model.enums.ErrorMessages.MAIL_IN_USE;

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

        if(users.isEmpty()){
            casoActual.remove(chat_id, "users_list");
            sender.sendMessage("No hay mas resultados", chat_id);
        }else {
            pasoActual.put(chat_id, pag+1);
            sender.sendMessage("Lista de usuarios - Página "+pag+"\n\n"+usersString+"\n\n/mas para mostrar más resultados", chat_id);
        }

    }

    public void processRegister(Long chat_id, boolean restart, String message, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "register");
            pasoActual.put(chat_id, 1);
        }

        String exit = "\n\nEscriba /exit para volver a comenzar";
        String usuario = "Indique su nombre de usuario deseado: [A-Za-z0-9_]+"; // [A-Za-z0-9_] equals \w
        String contrasenia = "indique su nueva contraseña (para iniciar sesion desde la pagina web): ";
        String email = "Indique su email: ";

        Integer step = pasoActual.get(chat_id);
        switch (step){
            case 1 :    //verificar no registro y pedir nuevo nombre de usuario
                String user = userService.findUsernameByTelegramID(chat_id);
                if(user == null){
                    pasoActual.put(chat_id, 2);
                    sender.sendMessage(usuario, chat_id);
                }else {
                    casoActual.remove(chat_id, "register");
                    sender.sendMessage("Usted ya se encuentra registrado bajo el usuario: "+user, chat_id);
                }
                break;

            case 2 :    //verificar nombre de usuario y pedir contraseña
                if(message.matches("\\w+")){
                    //todo verificar que el username no este en el hashmap temporal de otro usuario
                    UserEntity userEntity = userService.getUserByUsername(message.toLowerCase());
                    if(userEntity != null){
                        sender.sendMessage("El usuario "+message+" ya esta en uso, elija otro."+exit, chat_id);
                        return;
                    }

                    username.put(chat_id, message);
                    pasoActual.put(chat_id, 3);
                    sender.sendMessage(contrasenia, chat_id);
                }else{
                    sender.sendMessage(usuario+exit, chat_id);
                }
                break;

            case 3:     //verificar contraseña y pedir email
                //todo hacer alguna verificacion regex
                password.put(chat_id, message);
                pasoActual.put(chat_id, 4);
                sender.sendMessage(email, chat_id);
                break;

            case 4:     //verificar email y registrar usuario
                //todo hacer alguna verificacion regex
                UserEntity userEntity = userService.findUserByEmail(message.toLowerCase());
                if(userEntity != null){
                    sender.sendMessage("El email ya esta en uso, elija otro."+exit, chat_id);
                    return;
                }

                //crear el usuario
                //todo asegurar que nadie mas haya tomado mi usuario reservado previamente
                String name = username.get(chat_id);
                userService.createUserTelegram(name, password.get(chat_id), message, chat_id);

                sender.sendMessage("Ahora eres para siempre: "+name, chat_id);

                username.remove(chat_id);
                password.remove(chat_id);
                casoActual.remove(chat_id, "register");
                break;


            default :
                sender.sendMessage("?????????", chat_id);
        }

    }
}
