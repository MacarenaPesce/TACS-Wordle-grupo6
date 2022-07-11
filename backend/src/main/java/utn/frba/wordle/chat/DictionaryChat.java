package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.DictionaryService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class DictionaryChat {
    //funcion de obtener definiciones de palabras en español y en ingles

    @Autowired
    TeleSender sender;
    @Autowired
    DictionaryService dictionaryService;

    /**
     * Paso hasta el que llegó un usuario dentro del caso de uso.
     */
    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    final HashMap<Long, StringBuilder> commandBuilder = new HashMap<>();

    public void processDictionary(String[] params, Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "definition");
            pasoActual.put(chat_id, 0);
            commandBuilder.remove(chat_id);
        }

        Integer valor = pasoActual.get(chat_id);
        if(valor == null || valor == 0){ // comando completo

            String comando = "/definition es|en word";
            String sintaxis = "Sintaxis: \n"+comando;
            if(params[0].equals("")){
                pasoActual.put(chat_id, 1);
                sender.sendMessageWithKB(comando+"\nObtenga la definición de una palabra a su antojo.\n\nElija el idioma: es o en", chat_id, "", "[[\"/definition en bot\"],[\"ES\",\"EN\"]]");
                return;
            }

            if(params.length != 2 || !params[0].matches("es|en") || !params[1].matches("[A-Za-z]+")){
                sender.sendMessage(sintaxis, chat_id, "");
                return;
            }

            String str = buildSolution(params);

            casoActual.remove(chat_id, "definition");
            sender.sendMessage(str, chat_id, "");
        }
        else{ // comando interactive
            interactive(chat_id, valor, params[0], casoActual);
        }

    }

    private String buildSolution(String[] params){              //todo separar las distintas definiciones con saltos de linea aunque sea

        Language language = null;
        if(params[0].equals("es"))
            language = Language.ES;
        if(params[0].equals("en"))
            language = Language.EN;

        String word = params[1];

        List<String> definitions = dictionaryService.getDefinitions(language, word);

        return definitions.toString();
    }

    private void interactive(Long chat_id, int step, String message, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String exit = "\n\nEscriba /exit para volver a comenzar";
        String idioma = "Elija el idioma:\n es|en";
        String word = "Escriba su palabra";

        switch(step){
            case 1 :    //elegir idioma
                if(message.equalsIgnoreCase("es") || message.equalsIgnoreCase("en")){
                    commandBuilder.put(chat_id, new StringBuilder(message.toLowerCase()+" "));
                    pasoActual.put(chat_id, 2);
                    sender.sendMessageDelKB(word, chat_id, "");
                }else{
                    sender.sendMessage(idioma+exit, chat_id, "");
                }
                break;

            case 2 :    //escribir palabra
                if(message.matches("[A-Za-z]+")){
                    commandBuilder.put(chat_id, commandBuilder.get(chat_id).append(message.toLowerCase()).append(" "));
                    //construir mensaje y dar la respuesta
                    String[] params = commandBuilder.get(chat_id).toString().split("\\s+");
                    processDictionary(params, chat_id, true, casoActual);
                    sender.sendMessage("Gracias por utilizar Wordle bot 😇🦾", chat_id, "");
                }else{
                    sender.sendMessage(word+exit, chat_id, "");
                }
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }

    }
}
