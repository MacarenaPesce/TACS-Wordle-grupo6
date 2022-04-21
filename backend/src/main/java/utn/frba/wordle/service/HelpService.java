package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.utils.WordFileReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class HelpService {

    public HelpSolutionDto solution(HelpRequestDto helpRequestDto, Language language) throws IOException {

        Set<String> posibleSolutions = findPossibleSolutions(language, helpRequestDto.getYellow(), helpRequestDto.getGrey(), helpRequestDto.getSolution());

        return HelpSolutionDto.builder()
                .possibleWords(posibleSolutions)
                .build();
    }

    public Set<String> findPossibleSolutions(Language lang, String yellow, String grey, String solutionSoFar) throws IOException {

        Set<String> posibleSolutions = null;
        if (lang.equals(Language.ES))
            posibleSolutions = WordFileReader.getSpanishWords();
        if (lang.equals(Language.EN))
            posibleSolutions = WordFileReader.getEnglishWords();


        posibleSolutions = matchWith(posibleSolutions, solutionSoFar);
        posibleSolutions = removeWithoutYellow(posibleSolutions, yellow);
        posibleSolutions = removeWithGray(posibleSolutions, grey);

        return posibleSolutions;
    }

    private Set<String> removeWithGray(Set<String> wordList, String grey){

        List<Character> greyLetters = grey.chars().mapToObj(e->(char)e).collect(Collectors.toList());

        for (int i = 0; i < greyLetters.size(); i++) {
            String letter = String.valueOf(greyLetters.get(i));
            wordList = wordList.stream().filter(word -> !word.contains(letter)).collect(Collectors.toSet());
        }

        return wordList;
    }


    private Set<String> removeWithoutYellow(Set<String> wordList, String yellow){

        List<Character> yellowLetters = yellow.chars().mapToObj(e->(char)e).collect(Collectors.toList());

        for (int i = 0; i < yellowLetters.size(); i++) {
            String letter = String.valueOf(yellowLetters.get(i));
            wordList = wordList.stream().filter(word -> word.contains(letter)).collect(Collectors.toSet());
        }

        return wordList;
    }

    private Set<String> matchWith(Set<String> wordList, String solutionSoFar){

        if(solutionSoFar.length() == 0){
            return wordList;
        }

        char letter_1 = solutionSoFar.charAt(0);
        char letter_2 = solutionSoFar.charAt(1);
        char letter_3 = solutionSoFar.charAt(2);
        char letter_4 = solutionSoFar.charAt(3);
        char letter_5 = solutionSoFar.charAt(4);

        if(letter_1 != '_')
            wordList = wordList.stream().filter(word -> (word.charAt(0) == letter_1)).collect(Collectors.toSet());

        if(letter_2 != '_')
            wordList = wordList.stream().filter(word -> (word.charAt(1) == letter_2)).collect(Collectors.toSet());

        if(letter_3 != '_')
            wordList = wordList.stream().filter(word -> (word.charAt(2) == letter_3)).collect(Collectors.toSet());

        if(letter_4 != '_')
            wordList = wordList.stream().filter(word -> (word.charAt(3) == letter_4)).collect(Collectors.toSet());

        if(letter_5 != '_')
            wordList = wordList.stream().filter(word -> (word.charAt(4) == letter_5)).collect(Collectors.toSet());

        return wordList;
    }

}
