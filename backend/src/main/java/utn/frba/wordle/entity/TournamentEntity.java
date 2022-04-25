package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;
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

    @Column(unique = true)
    String name;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    TournamentType type;

    @Column
    Date start;

    @Column
    Date finish;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    UserEntity owner;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "Tournament_User",
            joinColumns = @JoinColumn(name = "Id_Tournament"),
            inverseJoinColumns = @JoinColumn(name = "Id_User"))
    private Set<UserEntity> members;

}
