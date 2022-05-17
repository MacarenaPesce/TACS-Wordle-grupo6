package utn.frba.wordle.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Registration",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = { "Id_Tournament", "Id_User" }) })
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_User", nullable = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_Tournament", nullable = false)
    private TournamentEntity tournament;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PunctuationEntity> punctuations;

    @Column
    private Date registered;
}
