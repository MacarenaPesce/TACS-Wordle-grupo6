package utn.frba.wordle.client;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SpanishDictionary {

    @Value("${api.dictionary.spanish}")
    String apiHost;

    @SneakyThrows
    public List<String> getDefinitions(String word) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";

        try (httpClient) {
            HttpGet request = new HttpGet(apiHost);

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
        return Collections.singletonList(result);
    }
}
