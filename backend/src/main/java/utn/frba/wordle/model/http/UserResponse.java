package utn.frba.wordle.model.http;

import lombok.*;
import utn.frba.wordle.model.dto.TournamentDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.model.pojo.State;
import utn.frba.wordle.model.pojo.TournamentType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;

    public UserResponse(UserDto dto) {
        UserResponse.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
    }
}
