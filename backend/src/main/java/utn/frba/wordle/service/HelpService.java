package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.HelpDto;
import utn.frba.wordle.model.enums.ErrorMessages;
import utn.frba.wordle.model.enums.Language;
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
     * Finds possible solutions.
     * @param language desired language
     * @return returns a set of strings with the possible solutions
     */
    @SneakyThrows
    public Set<String> solution(HelpDto normalized, Language language) {
        return findPossibleSolutions(language, normalized.getYellow(), normalized.getGrey(), normalized.getSolution());
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

        Set<String> possibleSolutions = null;
        if (lang.equals(Language.ES))
            possibleSolutions = WordFileReader.getSpanishWords();
        if (lang.equals(Language.EN))
            possibleSolutions = WordFileReader.getEnglishWords();


        possibleSolutions = matchWith(possibleSolutions, solutionSoFar);
        possibleSolutions = removeWithoutYellow(possibleSolutions, yellow);
        possibleSolutions = removeWithGray(possibleSolutions, grey);

        return possibleSolutions;
    }



    /**
     * Removes all words from the set which contain any of the letters included in the grey field.
     * @param wordList set of strings which can contain or not the letters in the grey field
     * @param grey letters that will be removed in the resulting set
     * @return set of strings where now, none of the strings contain any of the letters in the grey field
     */
    private Set<String> removeWithGray(Set<String> wordList, String grey){

        List<Character> greyLetters = grey.chars().mapToObj(e->(char)e).collect(Collectors.toList());

        for (Character greyLetter : greyLetters) {
            String letter = String.valueOf(greyLetter);
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

        for (Character yellowLetter : yellowLetters) {
            String letter = String.valueOf(yellowLetter);
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

    public HelpDto normalizeInput(String amarillas, String grises, String solucion){
        //remove non letters and make lowercase
        String yellow = amarillas.replaceAll("[^A-Za-z]+", "").toLowerCase();
        String grey = grises.replaceAll("[^A-Za-z]+", "").toLowerCase();
        String solution = solucion.replaceAll("[^A-Za-z_]+", "").toLowerCase();

        if( !(solution.length() == 5 || solution.length() == 0)){
            throw new BusinessException(String.format(ErrorMessages.INCORRECT_SOLUTION_LENGHT.getDescription(), solution, solution.length()));
        }

        //remove duplicates
        yellow = Arrays.stream(yellow.split(""))
                .distinct()
                .collect(Collectors.joining());
        grey = Arrays.stream(grey.split(""))
                .distinct()
                .collect(Collectors.joining());

        return HelpDto.builder()
                .solution(solution)
                .grey(grey)
                .yellow(yellow)
                .build();
    }
}
