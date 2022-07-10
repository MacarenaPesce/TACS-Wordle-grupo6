package utn.frba.wordle.model.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder
@ToString
public class UserDto {

    private final Long id;
    private final String username;
    private final String email;

    public String toStringTelegram(){
        return id+" - "+username;
    }

    public String toStringInfoMarkdownV1(){
        return "_Presentamos la info del usuario número "+id+"_\n\n"+
                "*username*: "+username+"\n\n"+
                "*email*: "+email;
    }
}
