package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.State;
import utn.frba.wordle.model.TournamentType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Tournament")
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    TournamentType type;

    @Enumerated(EnumType.STRING)
    State state;

    @Column
    Date start;

    @Column
    Date finish;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_User", nullable = false)
    UserEntity owner;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<RegistrationEntity> registrations;
}
