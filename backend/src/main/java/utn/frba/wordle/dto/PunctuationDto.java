package utn.frba.wordle.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class PunctuationDto {

    private RegistrationDto registration;
    private Long punctuation;
    private Date date;
}
