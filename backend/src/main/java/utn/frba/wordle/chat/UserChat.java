package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.UserDto;
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

    //al momento del primer registro agregar forma de reutilizar un usuario que ya existia en el frontend
    //agregar forma de devolver o restaurar mi contraseña, para poder usar el mismo usuario en el frontend

    @Autowired
    TeleSender sender;
    @Autowired
    UserService userService;
    final HashMap<Long, Integer> paginaActual = new HashMap<>();

    public void processUsersList(Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "users_list");
            paginaActual.put(chat_id, 1);
        }

        Integer pag = paginaActual.get(chat_id);
        List<UserDto> users = userService.getAllWithPagination(25, pag);
        String usersString = users.stream().map(UserDto::toStringTelegram).collect(Collectors.joining("\n"));

        if(users.isEmpty()){
            casoActual.remove(chat_id, "users_list");
            sender.sendMessage("No hay mas resultados", chat_id);
        }else {
            paginaActual.put(chat_id, pag+1);
            sender.sendMessage("Lista de usuarios - Página "+pag+"\n\n"+usersString+"\n\n/mas para mostrar más resultados", chat_id);
        }

    }
}
