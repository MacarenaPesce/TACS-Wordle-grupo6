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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EnglishDictionaryClient {

    @SneakyThrows
    public List<String> getDefinitions(String word) {
        String response = getRestDictionaryResponse(word);
        return parseDefinitions(response);
    }

    private List<String> parseDefinitions(String result) {
        List<String> definitions = new ArrayList<>();

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

    private String getRestDictionaryResponse(String word) throws IOException {
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
                    result = EntityUtils.toString(entity);
                }
            }
        }
        return result;
    }
}
