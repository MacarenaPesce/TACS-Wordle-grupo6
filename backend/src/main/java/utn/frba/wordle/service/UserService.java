package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.*;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.repository.UserRepository;

import java.util.*;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    TournamentRepository tournamentRepository;

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

    public List<TournamentEntity> getMyTournamets(Long userId){
        List<TournamentEntity> tournaments = tournamentRepository.findByUserName(userId);
        return tournaments;
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

    public static UserDto mapToDto(UserEntity user) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    public static UserEntity mapToEntity(UserDto user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    public UserEntity findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public UserEntity findUserByID(Long userId) {
        UserEntity user;
        try {
            user = userRepository.findById(userId).orElseThrow();
        }catch (NoSuchElementException e){
            throw new BusinessException("The requested user ID is not found.");
        }
        return user;
    }

    public UserDto findUser(Long userId) {
        return mapToDto(userRepository.findById(userId).orElseThrow());
    }

    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Set<UserDto> getTournamentMembers(Long tournamentId) {
        return mapToDto(userRepository.getTournamentMembers(tournamentId));
    }

    private Set<UserDto> mapToDto(List<UserEntity> entities) {
        Set<UserDto> dtos = new HashSet<>(Collections.emptySet());
        for(UserEntity user:entities){
            dtos.add(UserService.mapToDto(user));
        }
        return dtos;
    }
}
