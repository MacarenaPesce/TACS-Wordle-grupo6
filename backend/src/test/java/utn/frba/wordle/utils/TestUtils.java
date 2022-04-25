package utn.frba.wordle.utils;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.UserService;

import java.util.Date;

public class TestUtils {

    public static final EasyRandom RANDOM = TestUtils.newEasyRandom();

    @Autowired
    UserService userService;

    private static EasyRandom newEasyRandom() {
        final EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.setRandomizationDepth(3);
        return new EasyRandom(parameters);
    }

    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static SessionDto getMockSession() {
        String username = "mockUser";
        String email = "mockMail@mail.com";
        Long userId = 100L;
        String accessToken = getJWTToken(username, email, userId, 100L);

        return SessionDto.builder()
                .token(accessToken)
                .username(username)
                .email(email)
                .userId(userId)
                .build();
    }

    public static SessionDto getValidSessionFromUser(UserDto user) {
        String accessToken = getJWTToken(user.getUsername(), user.getEmail(), user.getId(), 100L);

        return SessionDto.builder()
                .token(accessToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

    private static String getJWTToken(String username, String email, Long userId, Long jwtExpiration) {

        String token = Jwts
                .builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 100))
                .signWith(SignatureAlgorithm.HS512,
                        AuthService.SECRET.getBytes()).compact();

        return "Bearer " + token;
    }
}