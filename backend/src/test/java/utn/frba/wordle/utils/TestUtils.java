package utn.frba.wordle.utils;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import utn.frba.wordle.model.dto.Session;
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

    public static Session getMockSession() {
        String username = "mockUser";
        String email = "mockMail@mail.com";
        Long userId = 100L;
        String accessToken = getJWTToken(username, email, userId);

        return Session.builder()
                .token(accessToken)
                .username(username)
                .email(email)
                .userId(userId)
                .build();
    }

    private static String getJWTToken(String username, String email, Long userId) {

        String token = Jwts
                .builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100 * 100))
                .signWith(SignatureAlgorithm.HS512,
                        AuthService.SECRET.getBytes()).compact();

        return "Bearer " + token;
    }
}