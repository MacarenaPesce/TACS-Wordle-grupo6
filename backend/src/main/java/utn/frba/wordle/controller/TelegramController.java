package utn.frba.wordle.controller;

import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.enums.Language;
import utn.frba.wordle.model.tele.Update;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static utn.frba.wordle.utils.RunAfterStartup.TOKEN;

@RestController
@RequestMapping("/api/telegram")
@CrossOrigin
public class TelegramController {
    //usar esta dependencia en el caso de necesitar todos los objetos ya modelados:
    //https://github.com/pengrad/java-telegram-bot-api/tree/master/library/src/main/java/com/pengrad/telegrambot/model

    private static final String API_URL = "https://api.telegram.org/bot";

    @Autowired
    HelpService helpService;

    @PostMapping("/")
    public ResponseEntity<String> postUpdates(@RequestBody Update update) throws IOException, URISyntaxException {

        String myString = new GsonBuilder().setPrettyPrinting().create().toJson(update);
        System.out.println("Update recibido: \n"+myString);

        String text = update.getMessage().getText();
        Long chat_id = update.getMessage().getChat().getId();

        if(text.matches("/[^ ](.*)")){
            //enviarMensaje("se recibe el comando: \n"+text.substring(1), chat_id);

            String[] params = text.substring(1).split("\\s+");
            processCommand(params, chat_id);

        }else {
            String mensajeEnvio = update.getMessage().getFrom().getFirst_name()+", su mensaje "+update.getMessage().getMessage_id()+" dice: \n"+text;
            enviarMensaje(mensajeEnvio, chat_id);
        }

        return new ResponseEntity<>(myString, HttpStatus.OK);
    }

    private void processCommand(String[] params, Long chat_id) throws IOException, URISyntaxException {
        switch(params[0])
        {
            case "help" :
                processHelp(params, chat_id);
                break;

            case "login" :
                enviarMensaje("Ejemplo de otro comando", chat_id);
                break;

            default :
                enviarMensaje("Comando no reconocido: \n"+params[0], chat_id);
        }
    }

    private void processHelp(String[] params, Long chat_id) throws IOException, URISyntaxException {
        String sintaxis = "Sintaxis: \n/help es|en _|amarillas _|grises _a_a_";
        if(params.length != 5 || !params[1].matches("es|en") || !params[2].matches("_|[A-Za-z]+") || !params[3].matches("_|[A-Za-z]+") || !params[4].matches("[A-Za-z_]{5}")){
            enviarMensaje(sintaxis, chat_id);
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

        StringBuilder strbul = new StringBuilder();
        for(String str : possibleSolutions)
        {
            strbul.append(str);
            strbul.append("\n");
        }
        //just for removing last comma
            strbul.setLength(strbul.length()-1);
        String str = strbul.toString();

        enviarMensaje(str, chat_id);
    }

    private void enviarMensaje(String mensaje, Long chat_id) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + TOKEN + "/sendMessage";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", chat_id.toString())
                .addParameter("text", mensaje)
                .build();
        request.setURI(uri);

        CloseableHttpResponse responseHttp = httpClient.execute(request);

        HttpEntity entity = responseHttp.getEntity();
        //if (entity != null)
            String response = EntityUtils.toString(entity);
            System.out.println("Response del get sendMessage: "+response);

        if(response.equals("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: message is too long\"}")){
            enviarMensaje(response, chat_id);
        }

    }


}
