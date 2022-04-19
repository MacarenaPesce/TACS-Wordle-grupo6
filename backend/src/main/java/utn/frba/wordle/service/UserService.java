package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.repository.UserRepository;

import java.util.Collections;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public PositionsResponseDto getPositions() {
        PositionDto positionsDto = PositionDto
                .builder()
                .userId(1)
                .points(10)
                .username("carlita")
                .build();

        LadderboardDto ladderboardDtos = LadderboardDto
                .builder()
                .name("pepis")
                .tourneyId(2)
                .positions(Collections.singletonList(positionsDto))
                .build();

        return PositionsResponseDto
                .builder()
                .tourneys(Collections.singletonList(ladderboardDtos))
                .build();
    }

    public UserDto createUser(LoginDto loginDto) {
        UserEntity newUser = UserEntity.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .username(loginDto.getUsername())
                .build();

        newUser = userRepository.save(newUser);

        return mapToDto(newUser);
    }

    public static UserDto mapToDto(UserEntity newUser) {
        return UserDto.builder()
                .email(newUser.getEmail())
                .username(newUser.getUsername())
                .id(newUser.getId())
                .build();
    }

    public UserEntity findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
