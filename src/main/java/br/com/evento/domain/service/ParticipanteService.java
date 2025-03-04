package br.com.evento.domain.service;

import br.com.evento.domain.model.Participante;
import br.com.evento.domain.model.Presenca;

import java.util.List;

public interface ParticipanteService {

    Participante salvar(Long eventoId, Participante participante);

    List<Participante> listar(Long eventoId);

    Participante buscarTodosPorEventoId(Long eventoId);

    Participante findParticipanteByEventoId(Long eventoId, Long participanteId);

    boolean excluir(Long eventoId);

    Participante editar(Long eventoId, Long participanteId, Participante participanteNovo);

    Participante salvarPresenca(Long eventoId, Long participanteId, Presenca presenca, boolean edicao);

    void excluirPresenca(Long presencaId);
}


