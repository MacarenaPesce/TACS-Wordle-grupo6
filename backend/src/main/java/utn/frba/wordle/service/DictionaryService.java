package utn.frba.wordle.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.client.SpanishDictionaryPage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;


@Service
@NoArgsConstructor
public class DictionaryService {

    @Autowired
    SpanishDictionaryPage spanishDictionary;

    public List <String> getDefinitions(Language language, String word) {

        if(language.equals(Language.ES)){
            //spanishDictionary = new SpanishDictionary();
            List<String> definitions = spanishDictionary.getDefinitions(word.toLowerCase());
            return definitions;
        }

        String url = "https://api.dictionaryapi.dev/api/v2/entries/" + language +"/" + word ;


        String definition = "";
        try {
            definition = peticionHttpGet(url);
        } catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
            System.out.print("No existe definicion");
            definition="No existe definicion";
        }

        return Collections.singletonList(definition);
    }

    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        // Esto es lo que vamos a devolver
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
            String word = items.get("word").getAsString();
            JsonArray meaningsArray = items.get("meanings").getAsJsonArray();
            JsonObject mean = meaningsArray.get(0).getAsJsonObject();
            String tipoPalabra = mean.get("partOfSpeech").getAsString();
            JsonArray definiciones = mean.get("definitions").getAsJsonArray();
            JsonObject definicionItem = definiciones.get(0).getAsJsonObject();
            String definition = definicionItem.get("definition").getAsString();

            System.out.println("definition: " + definition);
         StringBuilder significado = new StringBuilder();
         significado.append(tipoPalabra);
         significado.append(": ");
         significado.append(definition);

        rd.close();
        // Regresar resultado, pero como cadena, no como StringBuilder
        System.out.println("significado: " + significado.toString());
        return significado.toString();
    }
}
