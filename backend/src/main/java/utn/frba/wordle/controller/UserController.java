package utn.frba.wordle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.model.dto.ResultDto;
import utn.frba.wordle.model.dto.SessionDto;
import utn.frba.wordle.model.dto.UserDto;
import utn.frba.wordle.model.pojo.Language;
import utn.frba.wordle.service.AuthService;
import utn.frba.wordle.service.PunctuationService;
import utn.frba.wordle.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PunctuationService punctuationService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@RequestParam(required = false) String username) {
        if (username==null){
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(userService.findByName(username), HttpStatus.OK);
        }
    }

    @GetMapping("/{userId}/getTodaysResult/{language}")     //TODO agregar tests para esta ruta
    public ResponseEntity<ResultDto> getTodaysResult(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Language language) {
        SessionDto session = AuthService.getSession(token);
        checkIDs(session, userId);

        Long result = punctuationService.getTodaysResult(userId, language);

        ResultDto dto = ResultDto.builder()
                .userId(userId)
                .result(result)
                .language(language)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private void checkIDs(SessionDto session, Long userId){
        Long tokenUserId = session.getUserId();
        if (!userId.equals(tokenUserId)){
            throw new BusinessException("Debe coincidir el user id del path ("+userId+"), con el user id del usuario logueado ("+tokenUserId+")");
        }
    }
}
