package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.ResultDto;
import utn.frba.wordle.dto.UserDto;
import utn.frba.wordle.entity.ResultEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.exception.BusinessException;
import utn.frba.wordle.repository.ResultRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@NoArgsConstructor
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserService userService;

    public ResultDto submitResults(Long userId, ResultDto resultDto) {

        UserDto user = userService.findUser(userId);
        UserEntity userEntity = UserService.mapToEntity(user);
        LocalDate now = LocalDate.now();

        List<ResultEntity> results = resultRepository.findTodayResults(userEntity, resultDto.getLanguage(), now);

        if(results != null && !results.isEmpty()){
            throw new BusinessException("The user already submitted his results.");
        }

        ResultEntity entity = ResultEntity.builder()
                .result(resultDto.getResult())
                .language(resultDto.getLanguage())
                .date(LocalDate.now())
                .user(UserService.mapToEntity(user))
                .build();

        entity = resultRepository.save(entity);

        return mapToDo(entity);
    }

    private ResultDto mapToDo(ResultEntity entity) {
        return ResultDto.builder()
                .language(entity.getLanguage())
                .result(entity.getResult())
                .id(entity.getId())
                .build();
    }
}
