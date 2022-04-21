package utn.frba.wordle.dto;

import lombok.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HelpRequestDto {

    private String yellow;
    private String grey;
    private String solution;

    public void normalizeInput(){
        //remove non letters and make lowercase
        yellow = yellow.replaceAll("[^A-Za-z]+", "").toLowerCase();
        grey = grey.replaceAll("[^A-Za-z]+", "").toLowerCase();
        solution = solution.replaceAll("[^A-Za-z_]+", "").toLowerCase();

        //remove duplicates
        yellow = Arrays.asList(yellow.split(""))
                .stream()
                .distinct()
                .collect(Collectors.joining());
        grey = Arrays.asList(grey.split(""))
                .stream()
                .distinct()
                .collect(Collectors.joining());

        if( !(solution.length() == 5 || solution.length() == 0)){
            throw new RuntimeException("solution can not have a lenght of "+solution.length());
        }

    }

}
