package utn.frba.wordle.utils;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import utn.frba.wordle.dto.LoginDto;
import utn.frba.wordle.dto.SessionDto;
import utn.frba.wordle.service.AuthService;

import java.util.Date;

public class TestUtils {

    public static final EasyRandom RANDOM = TestUtils.newEasyRandom();

    private static EasyRandom newEasyRandom() {
        final EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.setRandomizationDepth(3);
        return new EasyRandom(parameters);
    }

    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static SessionDto getValidSession() {
        String accessToken = getJWTToken("ADMIN_USER", "ADMIN_EMAIL", 35L, 100L);

        return SessionDto.builder()
                .token(accessToken)
                .username("ADMIN_USER")
                .email("ADMIN_EMAIL")
                .userId(35L)
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