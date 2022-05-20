package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.*;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.repository.UserRepository;

import java.util.*;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(LoginDto loginDto) {
        UserEntity newUser = UserEntity.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .username(loginDto.getUsername().toLowerCase())
                .build();

        newUser = userRepository.save(newUser);

        return mapToDto(newUser);
    }

    public UserEntity findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username.toLowerCase(), password);
    }

    public UserDto findUser(Long userId) {
        return mapToDto(userRepository.findById(userId).orElseThrow());
    }

    public UserEntity findUserById(Long userId) {
        UserEntity user;
        try {
            user = userRepository.findById(userId).orElseThrow();
        }catch (NoSuchElementException e){
            throw new BusinessException("The requested user ID is not found.");
        }
        return user;
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> getTournamentMembers(Long tournamentId) {
        return mapToDto(userRepository.getTournamentMembers(tournamentId));
    }

    public List<UserDto> findAll() {
        return new ArrayList<>(mapToDto((List<UserEntity>) userRepository.findAll()));
    }

    public List<UserDto> findByName(String username) {
        List<UserEntity> users = userRepository.findByPartialUsername(username.toLowerCase());
        return mapToDto(users);
    }

    public static UserEntity mapToEntity(UserDto user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    private List<UserDto> mapToDto(List<UserEntity> entities) {
        List<UserDto> dtos = new ArrayList<>();
        for(UserEntity user:entities){
            dtos.add(UserService.mapToDto(user));
        }
        return dtos;
    }

    public static UserDto mapToDto(UserEntity user) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

}
