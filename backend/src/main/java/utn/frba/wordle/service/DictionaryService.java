package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.client.EnglishDictionaryPage;
import utn.frba.wordle.client.SpanishDictionaryPage;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;

import java.util.List;


@Service
@NoArgsConstructor
public class DictionaryService {

    @Autowired
    SpanishDictionaryPage spanishDictionary;

    @Autowired
    EnglishDictionaryPage englishDictionary;

    public List<String> getDefinitions(Language language, String word)   {

        switch (language)
        {
            case ES:
                return spanishDictionary.getDefinitions(word.toLowerCase());
            case EN:
                return englishDictionary.getDefinitions(word.toLowerCase());
            default:
                throw new BusinessException(String.format("Not implemented language: %s", language));
        }
    }
}
