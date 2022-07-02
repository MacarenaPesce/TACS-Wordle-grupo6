package utn.frba.wordle.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static utn.frba.wordle.utils.RunAfterStartup.TOKEN;

@Service
public class TeleSender {

    private static final String API_URL = "https://api.telegram.org/bot";

    public void sendMessage(String mensaje, Long chat_id) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + TOKEN + "/sendMessage";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", chat_id.toString())
                .addParameter("text", mensaje)
                .addParameter("parse_mode", "Markdown")
                .build();
        request.setURI(uri);

        CloseableHttpResponse responseHttp = httpClient.execute(request);

        HttpEntity entity = responseHttp.getEntity();
        //if (entity != null)
        String response = EntityUtils.toString(entity);
        System.out.println("Response del get sendMessage: "+response);

        handleErrors(response, chat_id);

    }

    private void handleErrors(String response, Long chat_id) throws IOException, URISyntaxException {

        if(response.equals("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: message is too long\"}")){
            sendMessage(response, chat_id);
        }

    }
}
