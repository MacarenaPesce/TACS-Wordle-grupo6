package utn.frba.wordle.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnglishDictionaryPage {

    @SneakyThrows
    public List<String>  getDefinitions(String word ) {
        List<String> significado = new ArrayList<>();
        String urlParaVisitar = "https://api.dictionaryapi.dev/api/v2/entries/EN/" + word;
        //peticionHttpGet(String urlParaVisitar)
        // Esto es lo que vamos a devolver
        System.out.println("recibi url: " + urlParaVisitar);
        StringBuilder resultado = new StringBuilder();
        // Crear un objeto de tipo URL
        URL url = new URL(urlParaVisitar);

        // Abrir la conexión e indicar que será de tipo GET
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        // Búferes para leer
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;
        // Mientras el BufferedReader se pueda leer, agregar contenido a resultado
        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }

        Gson gson =new Gson();
        JsonArray miJsonArray = gson.fromJson(resultado.toString(), JsonArray.class);
        System.out.println( miJsonArray.size());
        JsonElement elemento = miJsonArray.get(0);
        JsonObject items = elemento.getAsJsonObject();
        String item = items.get("word").getAsString();
        JsonArray meaningsArray = items.get("meanings").getAsJsonArray();
        JsonObject mean = meaningsArray.get(0).getAsJsonObject();
        String tipoPalabra = mean.get("partOfSpeech").getAsString();
        JsonArray definiciones = mean.get("definitions").getAsJsonArray();
        JsonObject definicionItem = definiciones.get(0).getAsJsonObject();
        String definition = definicionItem.get("definition").getAsString();

        System.out.println("definition: " + definition);
//        List<String> significado = new ArrayList<>();
        significado.add(tipoPalabra);
        significado.add(": ");
        significado.add(definition);

        rd.close();

        System.out.println("significado: " + significado);
        return significado;
    }
}
