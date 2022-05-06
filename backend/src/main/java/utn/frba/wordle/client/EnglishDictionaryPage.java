package utn.frba.wordle.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EnglishDictionaryPage {

    @SneakyThrows
    public List<String>  getDefinitions(String word ) {
        List<String> definitions = new ArrayList<>();
        String destinationUrl = "https://api.dictionaryapi.dev/api/v2/entries/EN/" + word;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";

        try (httpClient) {
            HttpGet request = new HttpGet(destinationUrl);

            request.addHeader("Accept", "application/json");
            request.addHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    result = EntityUtils.toString(entity);
                }
            }
        }

        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<EnglishDictionaryResponse>>(){}.getType();
        ArrayList<EnglishDictionaryResponse> responses = gson.fromJson(result, userListType);
        AtomicReference<String> partialDefinition = new AtomicReference<>("");
        responses.stream()
                .flatMap(response -> response.meanings.stream())
                .flatMap(meaning -> {
                                partialDefinition.set(meaning.partOfSpeech + ":");
                                return meaning.definitions.stream();
                })
                .forEach(definition -> definitions.add(partialDefinition + definition.definition));

        return definitions;
    }
}
