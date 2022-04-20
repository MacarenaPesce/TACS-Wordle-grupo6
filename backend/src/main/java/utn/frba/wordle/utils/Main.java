package utn.frba.wordle.utils;


import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("buen dia");

        System.out.println(WordFileReader.getSpanishWords());
        System.out.println(WordFileReader.getEnglishWords());

        String solutionSoFar = "A_V_F";
        char letter_1 = solutionSoFar.charAt(0);
        char letter_2 = solutionSoFar.charAt(1);
        char letter_3 = solutionSoFar.charAt(2);
        char letter_4 = solutionSoFar.charAt(3);
        char letter_5 = solutionSoFar.charAt(4);
        System.out.println(letter_3);

        if(letter_2 == '_')
            System.out.println("Es _");
        else
            System.out.println("No es _");

        HelpService help = new HelpService();
        /*
        List<String> palabras = help.matchWith(WordFileReader.getEnglishWords(), "j____");
        System.out.println("soluciones: ");
        System.out.println(palabras);

        List<Character> yellowLetters = "yellow".chars().mapToObj(e->(char)e).collect(Collectors.toList());

        System.out.println(yellowLetters);

        List<String> words = help.removeWithoutYellow(WordFileReader.getEnglishWords(), "ueitttt");
        System.out.println("soluciones: ");
        System.out.println(words);

        List<String> wordsaa = help.removeWithGray(WordFileReader.getEnglishWords(), "Aaeci");
        System.out.println("soluciones: ");
        System.out.println(wordsaa);
        */

        List<String> list = help.findPossibleSolutions(Language.ES, "tu", "ea", "____s");
        System.out.println("soluciones: ");
        System.out.println(list);

    }
}
