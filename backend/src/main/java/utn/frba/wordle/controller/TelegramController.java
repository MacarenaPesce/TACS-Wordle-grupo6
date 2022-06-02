package utn.frba.wordle.controller;

import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.model.tele.Update;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static utn.frba.wordle.utils.RunAfterStartup.TOKEN;

@RestController
@RequestMapping("/api/telegram")
@CrossOrigin
public class TelegramController {
    //usar esta dependencia en el caso de necesitar todos los objetos ya modelados:
    //https://github.com/pengrad/java-telegram-bot-api/tree/master/library/src/main/java/com/pengrad/telegrambot/model

    private static final String API_URL = "https://api.telegram.org/bot";


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
                enviarMensaje("Comando help solicitado", chat_id);
                break;

            case "login" :
                enviarMensaje("Ejemplo de otro comando", chat_id);
                break;

            default :
                enviarMensaje("Comando no reconocido: \n"+params[0], chat_id);
        }
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

        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        //if (entity != null)
            System.out.println("Response del get sendMessage: "+EntityUtils.toString(entity));

    }


}
