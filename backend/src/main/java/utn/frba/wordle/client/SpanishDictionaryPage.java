package utn.frba.wordle.client;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpanishDictionaryPage {

    @Value("${api.dictionary.spanish}")
    String apiHost;

    @SneakyThrows
    public List<String> getDefinitions(String word) {
        List<String> definitions = new ArrayList<>();

        String url = apiHost + String.valueOf(word.charAt(0)).toUpperCase() + "/" + word + "-1.html";

        Document doc = Jsoup.connect(url)
                .ignoreContentType(true)
                .get();

        for (Element line : doc.select(".definicionPalabra")) {
            definitions.add(line.text());
        }
    return definitions;
    }
}
