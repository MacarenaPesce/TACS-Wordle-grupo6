package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.RegistrationDto;
import utn.frba.wordle.entity.RegistrationEntity;
import utn.frba.wordle.entity.TournamentEntity;
import utn.frba.wordle.entity.UserEntity;
import utn.frba.wordle.repository.RegistrationRepository;
import utn.frba.wordle.repository.TournamentRepository;
import utn.frba.wordle.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    UserRepository userRepository;

    public List<RegistrationDto> getRegistrationsFromUser(Long userId) {
        return mapToDto(getRegistrationsEntityFromUser(userId));
    }

    public List<RegistrationEntity> getRegistrationsEntityFromUser(Long userId) {
        return registrationRepository.getAllByUser(userId);
    }

    public List<RegistrationDto> getRegistrationsFromTournament(Long tourneyId) {
        return mapToDto(registrationRepository.getAllByTournament(tourneyId));
    }


    public RegistrationEntity addMember(Long tournamentId, Long userId, Date date) {
        TournamentEntity tournamentEntity = tournamentRepository.findById(tournamentId).orElseThrow();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        RegistrationEntity entity = RegistrationEntity.builder()
                .registered(new Date())
                .tournament(tournamentEntity)
                .user(userEntity)
                .build();
        return registrationRepository.save(entity);
    }

    private List<RegistrationDto> mapToDto(List<RegistrationEntity> entities) {
        List<RegistrationDto> dtos = new ArrayList<>();
        for(RegistrationEntity entity: entities){
            RegistrationDto dto = RegistrationDto.builder()
                    .id(entity.getId())
                    .tournamentId(entity.getTournament().getId())
                    .punctuations(new ArrayList<>(entity.getPunctuations()))
                    .registered(entity.getRegistered())
                    .user(entity.getUser())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }
}
