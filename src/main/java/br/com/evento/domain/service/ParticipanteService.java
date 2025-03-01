package br.com.evento.domain.service;

import br.com.evento.domain.model.Evento;
import br.com.evento.domain.model.Participante;

import java.util.List;

public interface ParticipanteService {

    Participante salvar(Long eventoId, Participante participante);

    List<Participante> listar(Long eventoId);

    Participante buscarPorId(Long id);

    boolean excluir(Long eventoId);

    Participante editar(Long id, Participante participanteNovo);
}


