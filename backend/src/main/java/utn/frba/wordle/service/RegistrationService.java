package utn.frba.wordle.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.frba.wordle.dto.RegistrationDto;
import utn.frba.wordle.entity.RegistrationEntity;
import utn.frba.wordle.repository.RegistrationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    public List<RegistrationDto> getRegistrationsFromUser(Long userId) {
        return mapToDto(getRegistrationsEntityFromUser(userId));
    }

    public List<RegistrationEntity> getRegistrationsEntityFromUser(Long userId) {
        return registrationRepository.getAllByUser(userId);
    }

    private List<RegistrationDto> mapToDto(List<RegistrationEntity> entities) {
        List<RegistrationDto> dtos = new ArrayList<>();
        for(RegistrationEntity entity: entities){
            RegistrationDto dto = RegistrationDto.builder()
                    .id(entity.getId())
                    .tournamentId(entity.getTournament().getId())
                    .registered(entity.getRegistered())
                    .user(entity.getUser())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }
}
