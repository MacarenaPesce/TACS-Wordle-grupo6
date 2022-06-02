package utn.frba.wordle.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
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

@RestController
@RequestMapping("/api/telegram")
@CrossOrigin
public class TelegramController {
    //usar esta dependencia en el caso de necesitar todos los objetos ya modelados:
    //https://github.com/pengrad/java-telegram-bot-api/tree/master/library/src/main/java/com/pengrad/telegrambot/model

    String TOKEN = "";
    String API_URL = "https://api.telegram.org/bot"+TOKEN+"/";


    @PostMapping("/")
    public ResponseEntity<String> postUpdates(@RequestBody Update update) throws IOException, URISyntaxException {

        String myString = new GsonBuilder().setPrettyPrinting().create().toJson(update);
        System.out.println("Update recibido: \n"+myString);

        enviarMensaje(myString, update.getMessage().getChat().getId());

        return new ResponseEntity<>(myString, HttpStatus.OK);
    }

    private void enviarMensaje(String mensaje, Long chat_id) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + "sendMessage";
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
