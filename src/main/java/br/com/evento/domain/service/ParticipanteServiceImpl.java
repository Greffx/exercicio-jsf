package br.com.evento.domain.service;

import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Participante;
import br.com.evento.domain.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//TODO: Muita utilizagem do eventoId de forma não performática, avaliar melhor solução
@Service
@Transactional(readOnly = true)
public class ParticipanteServiceImpl implements ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    private static final String MSG_NOT_FOUND =
            "Participante não encontrado com esse ID.";

    @Transactional
    @Override
    public Participante salvar(Long eventoId, Participante participante) {
        if (participante.getEventoId() == null)
            participante.setEventoId(eventoId);

        var participantes = participanteRepository.findByEvento(eventoId);

        //verificação de CPF
        if (participantes != null && !participantes.isEmpty()) {
            for (var part : participantes) {
                if (part.getId() != participante.getId()) {
                    if (part.getCpf().equals(participante.getCpf()))
                        throw new BusinessException("CPF inválido.");

                    if (part.getEmail().equals(participante.getEmail()))
                        throw new BusinessException("E-mail inválido.");
                }

            }
        }

        return participanteRepository.save(participante);
    }

    @Transactional
    @Override
    public boolean excluir(Long id) {
        var participante = this.buscarTodosPorEventoId(id);

        try {
            participanteRepository.delete(participante);

            if (participante != null)
                return false;

            return true;
        } catch (DataIntegrityViolationException exc) {
            throw new BusinessException(exc.getMessage());
        }
    }

    @Override
    public Participante editar(Long eventoId, Long participanteId, Participante participanteNovo) {
        var participanteAtual = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));

        participanteAtual.setNome(participanteNovo.getNome());
        participanteAtual.setCpf(participanteNovo.getCpf());
        participanteAtual.setEmail(participanteNovo.getEmail());

        return this.salvar(eventoId, participanteAtual);
    }

    @Override
    public List<Participante> listar(Long eventoId) {
        return participanteRepository.findByEvento(eventoId);
    }

    @Transactional
    @Override
    public Participante buscarTodosPorEventoId(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));
    }

    @Override
    public Participante findParticipanteByEventoId(Long eventoId, Long participanteId) {
        return participanteRepository.findParticipanteByEventoId(eventoId, participanteId);
    }
}
