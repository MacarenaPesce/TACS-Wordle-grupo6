package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.entity.UserEntity;
import utn.frba.wordle.repository.UserRepository;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(String username, String password, String mail) {
        UserEntity newUser = UserEntity.builder()
                .email(mail.toLowerCase())
                .password(hashPassword(password))
                .username(username.toLowerCase())
                .build();

        newUser = userRepository.save(newUser);

        return mapToDto(newUser);
    }

    public void createUserTelegram(String username, String password, String mail, Long tele_id) {
        UserEntity newUser = UserEntity.builder()
                .email(mail.toLowerCase())
                .password(hashPassword(password))
                .username(username.toLowerCase())
                .telegram_userid(tele_id)
                .build();

        userRepository.save(newUser);
        return;
    }

    public UserEntity findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username.toLowerCase(), hashPassword(password));
    }

    public UserDto findUser(Long userId) {
        return mapToDto(findUserEntity(userId));
    }

    public UserEntity findUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public Long findUseridByTelegramID(Long userId) {
        return userRepository.findUseridByTelegramId(userId);
    }
    public String findUsernameByTelegramID(Long userId) {
        return userRepository.findUsernameByTelegramId(userId);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username.toLowerCase());
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
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

    public List<UserDto> findByNameWithPagination(String username, Integer maxResults, Integer actualPage) {
        Integer offset = (actualPage - 1) * maxResults;
        List<UserEntity> users = userRepository.findByPartialUsernameWithPagination(username.toLowerCase(), offset, maxResults);
        return mapToDto(users);
    }

    public List<UserDto> getAllWithPagination(Integer maxResults, Integer actualPage) {
        Integer offset = (actualPage - 1) * maxResults;
        List<UserEntity> users = userRepository.getAllWithPagination(offset, maxResults);
        return mapToDto(users);
    }

    public Integer findByNameTotalPages(String username, Integer maxResults) {
        Integer totalResults = userRepository.findByNameTotalPages(username);
        int pages = totalResults / maxResults;
        return Math.toIntExact(Math.round(Math.ceil(pages)));
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

    @SneakyThrows
    public String hashPassword(String password)  {

        String generatedPassword;
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(password.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        generatedPassword = sb.toString();
        return generatedPassword;
    }
}
