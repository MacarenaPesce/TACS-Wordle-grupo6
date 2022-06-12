package utn.frba.wordle.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.TeleSender;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HelpChat {

    @Autowired
    TeleSender sender;
    @Autowired
    HelpService helpService;

    public void processHelp(String[] params, Long chat_id) throws IOException, URISyntaxException {
        String sintaxis = "Sintaxis: \n/help es|en _|amarillas _|grises _a_a_";
        if(params.length != 5 || !params[1].matches("es|en") || !params[2].matches("_|[A-Za-z]+") || !params[3].matches("_|[A-Za-z]+") || !params[4].matches("[A-Za-z_]{5}")){
            sender.sendMessage(sintaxis, chat_id);
            return;
        }

        String yellow = params[2].toLowerCase();
        String grey = params[3].toLowerCase();
        String solution = params[4].toLowerCase();

        if(yellow.equals("_"))
            yellow = "";
        if(grey.equals("_"))
            grey = "";

        //remove duplicates     //todo juntar codigo con helpController
        yellow = Arrays.stream(yellow.split(""))
                .distinct()
                .collect(Collectors.joining());
        grey = Arrays.stream(grey.split(""))
                .distinct()
                .collect(Collectors.joining());

        HelpDto dto = HelpDto.builder()
                .solution(solution)
                .grey(grey)
                .yellow(yellow)
                .build();

        Language language = null;
        if(params[1].equals("es"))
            language = Language.ES;
        if(params[1].equals("en"))
            language = Language.EN;

        Set<String> possibleSolutions = helpService.solution(dto, language);

        String str;
        if(possibleSolutions.size() > 0) {
            StringBuilder strbul = new StringBuilder();
            for (String string : possibleSolutions) {
                strbul.append(string);
                strbul.append("\n");
            }
            //just for removing last comma
            strbul.setLength(strbul.length() - 1);
            str = strbul.toString();
        }else {
            str = "No hay coincidencias";
        }


        sender.sendMessage(str, chat_id);
    }
}
