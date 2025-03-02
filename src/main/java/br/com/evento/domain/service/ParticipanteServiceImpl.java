package br.com.evento.domain.service;

import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Participante;
import br.com.evento.domain.model.Presenca;
import br.com.evento.domain.repository.EventoRepository;
import br.com.evento.domain.repository.ParticipanteRepository;
import br.com.evento.domain.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

//TODO: Muita utilizagem do eventoId de forma não performática, avaliar melhor solução
@Service
@Transactional(readOnly = true)
public class ParticipanteServiceImpl implements ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private EventoRepository eventoRepository;


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
                if (!part.getId().equals(participante.getId())) {
                    if (eventoId.equals(part.getEventoId())) {
                        if (part.getCpf().equals(participante.getCpf()))
                            throw new BusinessException("CPF inválido.");

                        if (part.getEmail().equals(participante.getEmail()))
                            throw new BusinessException("E-mail inválido.");
                    }
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
        var participantes = participanteRepository.findByEvento(eventoId);

        for (var participante : participantes) {
            participante.setPresencas(
                    presencaRepository.findTodosByParticipante(participante.getId()));
        }

        return participantes;
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

    @Transactional
    @Override
    public Participante salvarPresenca(Long eventoId, Long participanteId, Presenca presenca, boolean edicao) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //find por Id
        var participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));

        //ver se existe mais algum com o mesmo CPF em outro evento
        var participantes = participanteRepository.findByCpf(participante.getCpf());
        var evento = eventoRepository.findById(eventoId).orElseThrow();
        var presencasByEvento = presencaRepository.findTodosByEvento(evento.getId());
        var presencasByParticipante = presencaRepository.findTodosByParticipante(participante.getId());

        var dataPresenca = presenca.getDataPresenca();

        //para ver se a data ta dentro da validação
        if (dataPresenca.isBefore(evento.getDataInicial()) ||
                dataPresenca.isAfter(evento.getDataFim())) {
            throw new BusinessException("Data precisa ser estar entre " +
                    formatador.format(evento.getDataInicial()) + " e " + formatador.format(evento.getDataFim()));
        }

        //comparo se já existe alguma presença daquele participante nesse evento
        if (presencasByEvento != null && !presencasByEvento.isEmpty()) {
            for (var prsnc : presencasByEvento) {
                if (prsnc.getParticipanteId().equals(participante.getId()) && !edicao) {
                    throw new BusinessException("Já existe um presenca com esse participante.");
                }
            }
        }

        //se já tem uma lista de participates com o mesmo CPF em outros eventos
        if (participantes != null && !participantes.isEmpty()) {
            for (var part : participantes) {
                if (!part.getEventoId().equals(evento.getId())) {
                    for (var pre : part.getPresencas()) {
                        if (pre.getDataPresenca().equals(presenca.getDataPresenca()))
                            throw new BusinessException(
                                    "Já foi registrado no mesmo dia, mas em evento diferente.");
                    }
                }
            }
        }

        presenca.setParticipanteId(participanteId);
        presenca.setEventoId(eventoId);

        presencaRepository.save(presenca);

        return participante;
    }

    @Transactional
    @Override
    public void excluirPresenca(Long presencaId) {
        var presenca = presencaRepository.findById(presencaId).orElseThrow(() ->
                new BusinessException("Presença não encontrada com esse ID."));
        try {
            presencaRepository.delete(presenca);

        } catch (DataIntegrityViolationException exc) {
            throw new BusinessException(exc.getMessage());
        }
    }
}
