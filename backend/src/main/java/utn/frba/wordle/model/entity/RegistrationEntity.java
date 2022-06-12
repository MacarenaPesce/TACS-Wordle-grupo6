package utn.frba.wordle.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(mappedBy = "registrations", fetch = FetchType.EAGER)
    private Set<PunctuationEntity> punctuations;

    @Column
    private Date registered;

    @Column
    private Long daysPlayed;

    @Column
    private Long totalScore;

    @Column
    private Date lastSubmittedScore;

}
