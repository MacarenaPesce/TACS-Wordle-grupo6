package utn.frba.wordle.entity;

import lombok.*;
import utn.frba.wordle.model.Language;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Result")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    Long  result;

    @Column
    Language language;

    @Column
    Date date;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    UserEntity owner;
}
