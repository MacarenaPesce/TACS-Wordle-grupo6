package utn.frba.wordle.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/telegram")
@CrossOrigin
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramController {

    String API_URL = "https://api.telegram.org/bot<token>/";
    String CHAT_ID = "<chat id>";


    @PostMapping("/")
    public ResponseEntity<String> postUpdates(@RequestBody String mensaje) throws IOException, URISyntaxException {

        System.out.println(mensaje);

        enviarMensaje(mensaje);

        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    private void enviarMensaje(String mensaje) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + "sendMessage";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", CHAT_ID)
                .addParameter("text", mensaje)
                .build();
        request.setURI(uri);

        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        //if (entity != null)
            System.out.println("Response del get sendMessage: "+EntityUtils.toString(entity));


    }


}
