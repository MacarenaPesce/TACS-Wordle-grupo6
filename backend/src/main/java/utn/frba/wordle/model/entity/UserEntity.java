package utn.frba.wordle.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private Long telegram_userid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RegistrationEntity> registrations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TournamentEntity> myTournaments;
}
