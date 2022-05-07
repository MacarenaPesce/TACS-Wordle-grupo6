package utn.frba.wordle.client;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpanishDictionaryPage {

    @SneakyThrows
    public List<String> getDefinitions(String word) {
        List<String> definitions = new ArrayList<>();

        String url = "https://es.wiktionary.org/wiki/" + word;
        Document doc = Jsoup.connect(url)
                .ignoreContentType(true)
                .get();

        for (Element line : doc.select("dl")) {
            definitions.add(line.text());
        }
        if (definitions.isEmpty()) {
            definitions.add("NoDefinition");
        }
    return definitions;
    }
}
