package utn.frba.wordle.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class TournamentDto {

    Integer tourneyId;
    String name;
    String language;
    String type;
    Date start;
    Date finish;


    private ArrayList<String> possibleWords;
}
