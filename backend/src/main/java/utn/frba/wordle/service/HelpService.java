package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.HelpRequestDto;
import utn.frba.wordle.dto.HelpSolutionDto;
import utn.frba.wordle.exception.BusinessException;
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

    /**
     * Finds possible solutions in DTO form.
     * @param helpRequestDto desired filters to match possible solutions
     * @param language desired language
     * @return returns the HelpSolutionDto with the possible solutions
     * @throws IOException inherited from the contained method findPossibleSolutions
     */
    public HelpSolutionDto solution(HelpRequestDto helpRequestDto, Language language) throws IOException {

        helpRequestDto = normalizeInput(helpRequestDto);

        Set<String> posibleSolutions = findPossibleSolutions(language, helpRequestDto.getYellow(), helpRequestDto.getGrey(), helpRequestDto.getSolution());

        return HelpSolutionDto.builder()
                .possibleWords(posibleSolutions)
                .build();
    }

    /**
     * Executes the word finder with specific data.
     * @param lang desired language
     * @param yellow known letters to be used in unknown locations
     * @param grey known letters to not be used
     * @param solutionSoFar green letters, contains each known-so-far letter in each of the 5 positions
     * @return set with the possible solutions, all words matching the filters
     * @throws IOException if the word list file can not be read, should never happen being in a static location
     */
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

    /**
     * Normalizes the input data to ensure it's processed correctly in lowercase,
     * avoid processing the same letter multiple times, avoid hackers, etc...
     * @param helpRequestDto DTO with the data captured from the wild world wide web
     * @return now safe DTO
     */
    public HelpRequestDto normalizeInput(HelpRequestDto helpRequestDto){
        //remove non letters and make lowercase
        String yellow = helpRequestDto.getYellow().replaceAll("[^A-Za-z]+", "").toLowerCase();
        String grey = helpRequestDto.getGrey().replaceAll("[^A-Za-z]+", "").toLowerCase();
        String solution = helpRequestDto.getSolution().replaceAll("[^A-Za-z_]+", "").toLowerCase();

        if( !(solution.length() == 5 || solution.length() == 0)){
            throw new BusinessException("solution '"+solution+"' can not have a lenght of "+solution.length());
        }

        //remove duplicates
        yellow = Arrays.stream(yellow.split(""))
                .distinct()
                .collect(Collectors.joining());
        grey = Arrays.stream(grey.split(""))
                .distinct()
                .collect(Collectors.joining());

        helpRequestDto.setGrey(grey);
        helpRequestDto.setYellow(yellow);
        helpRequestDto.setSolution(solution);

        return helpRequestDto;
    }

    /**
     * Removes all words from the set which contain any of the letters included in the grey field.
     * @param wordList set of strings which can contain or not the letters in the grey field
     * @param grey letters that will be removed in the resulting set
     * @return set of strings where now, none of the strings contain any of the letters in the grey field
     */
    private Set<String> removeWithGray(Set<String> wordList, String grey){

        List<Character> greyLetters = grey.chars().mapToObj(e->(char)e).collect(Collectors.toList());

        for (int i = 0; i < greyLetters.size(); i++) {
            String letter = String.valueOf(greyLetters.get(i));
            wordList = wordList.stream().filter(word -> !word.contains(letter)).collect(Collectors.toSet());
        }

        return wordList;
    }

    /**
     * Removes all words from the set which do not contain every letter included in the yellow field.
     * @param wordList set of strings which can contain or not the letters in the yellow field
     * @param yellow letters that have to be included in the resulting set
     * @return set of strings where now, every string contains every letter in the yellow field
     */
    private Set<String> removeWithoutYellow(Set<String> wordList, String yellow){

        List<Character> yellowLetters = yellow.chars().mapToObj(e->(char)e).collect(Collectors.toList());

        for (int i = 0; i < yellowLetters.size(); i++) {
            String letter = String.valueOf(yellowLetters.get(i));
            wordList = wordList.stream().filter(word -> word.contains(letter)).collect(Collectors.toSet());
        }

        return wordList;
    }

    /**
     * Filters a set of words, using the solutionSoFar as a wildcard.
     * @param wordList set of words which may or may not, be contained in the wildcard
     *                 (most of them should not, for our single intended use case)
     * @param solutionSoFar 5 letter wildcard "A_G_O"
     * @return set of words which now are contained in the wildcard
     */
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
