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

    private void sendMessageFull(String mensaje, Long chat_id, String parse_mode, HttpGet request) throws IOException, URISyntaxException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse responseHttp = httpClient.execute(request);

        HttpEntity entity = responseHttp.getEntity();
        //if (entity != null)
        String response = EntityUtils.toString(entity);
        System.out.println("Response del get sendMessage: "+response);

        handleErrors(response, chat_id);

    }

    public void sendMessage(String mensaje, Long chat_id, String parse_mode) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + TOKEN + "/sendMessage";
        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", chat_id.toString())
                .addParameter("text", mensaje)
                .addParameter("parse_mode", parse_mode)
                .build();
        request.setURI(uri);
        sendMessageFull(mensaje, chat_id, parse_mode, request);
    }

    public void sendMessageWithKB(String mensaje, Long chat_id, String parse_mode, String KB) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + TOKEN + "/sendMessage";
        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", chat_id.toString())
                .addParameter("text", mensaje)
                .addParameter("parse_mode", parse_mode)
                .addParameter("reply_markup", "{\"resize_keyboard\":true,\"one_time_keyboard\":true,\"keyboard\":"+KB+"}")
                .build();
        request.setURI(uri);
        sendMessageFull(mensaje, chat_id, parse_mode, request);
    }

    public void sendMessageDelKB(String mensaje, Long chat_id, String parse_mode) throws IOException, URISyntaxException {
        String destinationUrl = API_URL + TOKEN + "/sendMessage";
        HttpGet request = new HttpGet(destinationUrl);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("chat_id", chat_id.toString())
                .addParameter("text", mensaje)
                .addParameter("parse_mode", parse_mode)
                .addParameter("reply_markup", "{\"remove_keyboard\":true}")
                .build();
        request.setURI(uri);
        sendMessageFull(mensaje, chat_id, parse_mode, request);
    }

    private void handleErrors(String response, Long chat_id) throws IOException, URISyntaxException {

        if(response.startsWith("{\"ok\":false")){
            sendMessage(response, chat_id, "");
        }

    }
}
