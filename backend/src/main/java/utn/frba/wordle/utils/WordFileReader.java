package utn.frba.wordle.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordFileReader {

    private WordFileReader() {
    }

    private static Set<String> spanishWords = null;
    private static Set<String> englishWords = null;

    public static Set<String> getSpanishWords() throws IOException {
        if (spanishWords == null)
            return spanishWords = Files.readAllLines(Paths.get("src/main/resources/5letter-spanish.list")).stream().collect(Collectors.toSet());
        return spanishWords;
    }

    public static Set<String> getEnglishWords() throws IOException {
        if (englishWords == null)
            return englishWords = Files.readAllLines(Paths.get("src/main/resources/5letter-english.list")).stream()
                                                                                                                .map(String::toLowerCase)
                                                                                                                .collect(Collectors.toSet());
        return englishWords;
    }

}