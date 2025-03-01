package br.com.evento.domain.service;

import br.com.evento.domain.model.Evento;
import br.com.evento.domain.model.Participante;

import java.time.LocalDate;
import java.util.List;

public interface ParticipanteService {

    Participante salvar(Participante participante);

    boolean excluir(Long id);

    Participante editar(Long id, Participante participanteNovo);

    List<Participante> listar(String nome, LocalDate dataInicio, LocalDate dataFim);

    Participante buscarPorId(Long id);

}


