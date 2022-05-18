package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Punctuation")
public class PunctuationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    Long punctuation;

    @Enumerated(EnumType.STRING)
    Language language;

    @Column
    LocalDate date;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable( name = "Registration_punctuation",
            joinColumns = @JoinColumn(name = "Id_punctuation"),
            inverseJoinColumns = @JoinColumn(name = "Id_registration"))
    private Set<RegistrationEntity> registrations;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_User", nullable = false)
    private UserEntity user;
}
