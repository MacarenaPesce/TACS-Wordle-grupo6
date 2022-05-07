package utn.frba.wordle.client;

import java.util.ArrayList;
import java.util.List;

public class EnglishDictionaryResponse {

    ArrayList<Meaning> meanings;

    public static class Meaning {
        String partOfSpeech;
        List<Definition> definitions;

        public static class Definition {
            String definition;
        }
    }


}
