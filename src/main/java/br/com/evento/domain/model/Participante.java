package br.com.evento.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
public class Participante implements Serializable {

    @Serial
    private static final long serialVersionUID = 6833179502616053084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participante_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "evento_id")
    private Long eventoId;

    /**
     *N pessoas podem ou não estar em 1 evento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id",
            referencedColumnName = "evento_id", insertable = false, updatable = false)
    private Evento evento;

    @JsonIgnore
    @OneToMany(mappedBy = "participante")
    private List<Presenca> presencas = new ArrayList<>();
}
