package br.com.evento.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Table
@Entity
public class Presenca implements Serializable {

    @Serial
    private static final long serialVersionUID = -2109529657072822959L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presenca_id", nullable = false)
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate dataPresenca;

    @Column(name = "participante_id", nullable = false)
    private Long participanteId;

    /**
     *N presencas podem ou não ter em 1 pessoa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id",
            referencedColumnName = "participante_id", insertable = false, updatable = false)
    private Participante participante;

}
