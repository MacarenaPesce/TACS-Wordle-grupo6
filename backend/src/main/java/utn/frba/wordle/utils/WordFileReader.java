package utn.frba.wordle.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordFileReader {

    private WordFileReader() {
    }

    private static List<String> spanishWords = null;
    private static List<String> englishWords = null;

    public static List<String> getSpanishWords() throws IOException {
        if (spanishWords == null)
            return spanishWords = Files.readAllLines(Paths.get("src/main/resources/5letter-spanish.list"));
        return spanishWords;
    }

    public static List<String> getEnglishWords() throws IOException {
        if (englishWords == null)
            return englishWords = Files.readAllLines(Paths.get("src/main/resources/5letter-english.list"));
        return englishWords;
    }

}