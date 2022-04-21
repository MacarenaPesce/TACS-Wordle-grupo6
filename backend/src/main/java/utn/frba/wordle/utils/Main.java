package utn.frba.wordle.utils;


import utn.frba.wordle.model.Language;
import utn.frba.wordle.service.HelpService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("buen dia");

        HelpService help = new HelpService();

        Set<String> list = help.findPossibleSolutions(Language.EN, "tu", "eaA", "____s");
        System.out.println("soluciones: ");
        System.out.println(list);


        List<String> list2 = Arrays.asList("aaa", "bbb", "aaa", "ccc");
        System.out.println(list2);

        Set<String> set = new HashSet<>(list2);
        System.out.println(set);

        String newstr = "Word#___$#$% Word 1234".replaceAll("[^A-Za-z_]+", "");
        System.out.println(newstr);

        String solution = "aaaaaabagcasgrrrvvbbbbbbb";
        String solution2;

        solution2 = Arrays.asList(solution.split(""))
                .stream()
                .distinct()
                .collect(Collectors.joining());
        System.out.println(solution2);
    }
}
