package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;
import utn.frba.wordle.model.TounamentType;

import javax.persistence.*;
import java.util.Date;

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
    TounamentType type;

    @Column
    Date start;

    @Column
    Date finish;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    UserEntity owner;
}
