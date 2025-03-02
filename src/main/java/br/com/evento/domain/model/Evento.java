package br.com.evento.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table
@Entity
@ToString(exclude = "participantes")
public class Evento implements Serializable {

    @Serial
    private static final long serialVersionUID = -3114218390807073860L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evento_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @JsonIgnore
    @OneToMany(mappedBy = "evento")
    private List<Participante> participantes = new ArrayList<>();

}
