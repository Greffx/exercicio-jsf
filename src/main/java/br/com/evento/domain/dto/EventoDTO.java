package br.com.evento.domain.dto;

import br.com.evento.domain.model.Evento;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2254865708910206634L;

    private Long id;

    private String nome;

    private LocalDate dataInicial;
    private LocalDate dataFim;

    /**APENAS EM DIAS*/
    private Long duracaoEvento;

    public static List<EventoDTO> toDTO(List<Evento> eventos) {
        var eventosDTO = new ArrayList<EventoDTO>();

        for (var evento : eventos) {
            var eventoDTO = new EventoDTO();

            eventoDTO.setId(evento.getId());
            eventoDTO.setNome(evento.getNome());
            eventoDTO.setDuracaoEvento(ChronoUnit.DAYS.between(evento.getDataInicial(), evento.getDataFim()));
            eventoDTO.setDataInicial(evento.getDataInicial());
            eventoDTO.setDataFim(evento.getDataFim());

            eventosDTO.add(eventoDTO);
        }

        return eventosDTO;
    }
}
