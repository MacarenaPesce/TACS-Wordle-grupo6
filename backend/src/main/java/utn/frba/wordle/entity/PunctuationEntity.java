package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column
    LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Id_Registration", nullable = false)
    private RegistrationEntity registration;
}
