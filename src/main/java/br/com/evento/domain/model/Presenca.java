package br.com.evento.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@ToString(exclude = {"participante", "evento"})
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

    @Column(name = "evento_id", nullable = false)
    private Long eventoId;

    /**
     * em % a possibilidade de ida
     */
    private Integer porcentagem;

    /**
     *N presencas podem ou não ter em 1 pessoa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id",
            referencedColumnName = "participante_id", insertable = false, updatable = false)
    private Participante participante;

    /**
     *N presencas podem ou não ter em 1 evento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id",
            referencedColumnName = "evento_id", insertable = false, updatable = false)
    private Evento evento;

}
