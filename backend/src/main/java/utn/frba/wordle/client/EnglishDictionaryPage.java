package utn.frba.wordle.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
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
        //peticionHttpGet(String destinationUrl)
        // Esto es lo que vamos a devolver
        StringBuilder result = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(destinationUrl);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<EnglishDictionaryResponse>>(){}.getType();

        ArrayList<EnglishDictionaryResponse> responses = gson.fromJson(result.toString(), userListType);

        AtomicReference<String> partialDefinition = new AtomicReference<>("");
        responses.stream()
                .flatMap(response -> response.meanings.stream())
                .flatMap(meaning -> {
                                partialDefinition.set(meaning.partOfSpeech + ":");
                                return meaning.definitions.stream();
                })
                .forEach(definition -> definitions.add(partialDefinition + definition.definition));

        rd.close();

        return definitions;
    }
}
