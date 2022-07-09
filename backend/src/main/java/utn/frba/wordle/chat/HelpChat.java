package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

@Service
public class HelpChat {

    @Autowired
    TeleSender sender;
    @Autowired
    HelpService helpService;

    /**
     * Paso hasta el que llegó un usuario dentro del caso de uso.
     */
    final HashMap<Long, Integer> pasoActual = new HashMap<>();

    final HashMap<Long, StringBuilder> commandBuilder = new HashMap<>();

    public void processHelp(String[] params, Long chat_id, boolean restart, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        if(restart){
            casoActual.put(chat_id, "help");
            pasoActual.put(chat_id, 0);
            commandBuilder.remove(chat_id);
        }

        Integer valor = pasoActual.get(chat_id);
        if(valor == null || valor == 0){ // comando completo

            String sintaxis = "Sintaxis: \n/help es|en _|amarillas _|grises _a_a_";
            if(params[0].equals("")){
                pasoActual.put(chat_id, 1);
                sender.sendMessage("Obtenga ayuda para resolver un wordle.\n\nOpcion 1: vuelva a escribir el comando completo:\n"+sintaxis+"\n\n Opcion 2: envie solo el primer parametro.\n\n En criollo: elija el idioma: es o en", chat_id, "");
                return;
            }

            if(params.length != 4 || !params[0].matches("es|en") || !params[1].matches("_|[A-Za-z]+") || !params[2].matches("_|[A-Za-z]+") || !params[3].matches("[A-Za-z_]{5}")){
                sender.sendMessage(sintaxis, chat_id, "");
                return;
            }


            String str = buildSolution(params);

            casoActual.remove(chat_id, "help");
            sender.sendMessage(str, chat_id, "");
        }
        else{ // comando interactive
            interactive(chat_id, valor, params[0], casoActual);
        }

    }

    private String buildSolution(String[] params){
        HelpDto dto = helpService.normalizeInput(params[1], params[2], params[3]);

        Language language = null;
        if(params[0].equals("es"))
            language = Language.ES;
        if(params[0].equals("en"))
            language = Language.EN;

        Set<String> possibleSolutions = helpService.solution(dto, language);

        return setToString(possibleSolutions);
    }

    private String setToString(Set<String> setOfStrings){
        if(setOfStrings.size() > 0) {
            StringBuilder strbul = new StringBuilder();
            for (String string : setOfStrings) {
                strbul.append(string);
                strbul.append("\n");
            }
            //just for removing last comma
            strbul.setLength(strbul.length() - 1);
            return strbul.toString();
        }else {
            return "No hay coincidencias";
        }
    }

    private void interactive(Long chat_id, int step, String message, HashMap<Long, String> casoActual) throws IOException, URISyntaxException {
        String exit = "\n\nEscriba /exit para volver a comenzar";
        String idioma = "Elija el idioma:\n es|en";
        String amarillas = "Elija las letras amarillas: _|[A-Za-z]+";
        String grises = "Elija las letras grises: _|[A-Za-z]+";
        String solution = "Digame cual es su solucion actual: [A-Za-z_]{5}";

        switch(step){
            case 1 :    //elegir idioma
                if(message.equalsIgnoreCase("es") || message.equalsIgnoreCase("en")){
                    commandBuilder.put(chat_id, new StringBuilder(message.toLowerCase()+" "));
                    pasoActual.put(chat_id, 2);
                    sender.sendMessage(amarillas, chat_id, "");
                }else{
                    sender.sendMessage(idioma+exit, chat_id, "");
                }
                break;

            case 2 :    //elegir amarillas
                if(message.matches("_|[A-Za-z]+")){
                    commandBuilder.put(chat_id, commandBuilder.get(chat_id).append(message.toLowerCase()).append(" "));
                    pasoActual.put(chat_id, 3);
                    sender.sendMessage(grises, chat_id, "");
                }else{
                    sender.sendMessage(amarillas+exit, chat_id, "");
                }
                break;

            case 3 :    //elegir grises
                if(message.matches("_|[A-Za-z]+")){
                    commandBuilder.put(chat_id, commandBuilder.get(chat_id).append(message.toLowerCase()).append(" "));
                    pasoActual.put(chat_id, 4);
                    sender.sendMessage(solution, chat_id, "");
                }else{
                    sender.sendMessage(grises+exit, chat_id, "");
                }
                break;

            case 4 :    //elegir solution
                if(message.matches("[A-Za-z_]{5}")){
                    commandBuilder.put(chat_id, commandBuilder.get(chat_id).append(message.toLowerCase()).append(" "));
                    //construir mensaje y dar la respuesta
                    String[] params = commandBuilder.get(chat_id).toString().split("\\s+");
                    processHelp(params, chat_id, true, casoActual);
                    sender.sendMessage("Gracias por utilizar Wordle bot 😇🦾", chat_id, "");
                }else{
                    sender.sendMessage(solution+exit, chat_id, "");
                }
                break;

            default :
                sender.sendMessage("Disculpame pero no te entendi", chat_id, "");
        }

    }
}
