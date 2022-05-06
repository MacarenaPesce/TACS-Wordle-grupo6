package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.EnglishDictionaryPage;
import utn.frba.wordle.client.SpanishDictionaryPage;
import utn.frba.wordle.model.Language;

import java.util.ArrayList;
import java.util.List;


@Service
@NoArgsConstructor
public class DictionaryService {

    @Autowired
    SpanishDictionaryPage spanishDictionary;


    EnglishDictionaryPage englishDictionary;

    public List<String> getDefinitions(Language language, String word)   {
        List<String> definitions = new ArrayList<>();

        if (language.equals(Language.ES)) {
            //spanishDictionary = new SpanishDictionary();
            definitions = spanishDictionary.getDefinitions(word.toLowerCase());
//            return definitions;
        } else  {
            try {
                definitions.add("NoDefinition");
                definitions = englishDictionary.getDefinitions(word.toLowerCase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(("this got: "+  definitions));

        return definitions;
    }
}
