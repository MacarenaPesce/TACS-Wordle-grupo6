package utn.frba.wordle.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Registration")
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_User", nullable = false, unique = true)
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_Tournament", nullable = false, unique = true)
    private TournamentEntity tournament;

    @Column
    private Date registered;
}
