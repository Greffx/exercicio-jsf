package br.com.evento.domain.service;

import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Participante;
import br.com.evento.domain.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        participante.setEventoId(eventoId);
        var participantes = participanteRepository.findByEvento(eventoId);

        //verificação de CPF
        if (participantes != null && !participantes.isEmpty()) {
            for (var part : participantes) {
                if (part.getCpf().equals(participante.getCpf()))
                    throw new BusinessException("CPF inválido.");

                if (part.getEmail().equals(participante.getEmail()))
                    throw new BusinessException("E-mail inválido.");
            }
        }

        return participanteRepository.save(participante);
    }

    @Transactional
    @Override
    public boolean excluir(Long id) {
        var participante = this.buscarPorId(id);

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
    public Participante editar(Long id, Participante participanteNovo) {
        return null;
    }

    @Override
    public List<Participante> listar(Long eventoId) {
        return participanteRepository.findByEvento(eventoId);
    }

    @Override
    public Participante buscarPorId(Long id) {
        return participanteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));
    }
}
