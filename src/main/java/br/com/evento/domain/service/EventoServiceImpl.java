package br.com.evento.domain.service;

import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Evento;
import br.com.evento.domain.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    private static final String MSG_NOT_FOUND =
            "Evento não encontrado com esse ID.";

    @Transactional
    @Override
    public Evento salvar(Evento evento) {
        if (evento.getDataInicial() == null ||
                evento.getDataInicial().isBefore(LocalDate.now()) ||
                evento.getDataFim() == null ||
                evento.getDataFim().isBefore(LocalDate.now()) ||
                evento.getDataFim().isBefore(evento.getDataInicial())) {
            throw new BusinessException("Erro ao salvar evento, verificar a data inicial e data fim.");
        } else if (evento.getNome() == null || evento.getNome().isEmpty()) {
            throw new BusinessException("Erro ao salvar evento, verificar o nome do evento.");
        }

        return eventoRepository.save(evento);
    }

    @Transactional
    @Override
    public boolean excluir(Long id) {
        var evento = this.buscarPorId(id);

        try {
            eventoRepository.delete(evento);

            if (evento != null)
                return false;

            return true;
        } catch (DataIntegrityViolationException exc) {
            throw new BusinessException(exc.getMessage());
        }
    }

    @Transactional
    @Override
    public Evento editar(Long id, Evento eventoNovo) {
        var eventoAtual = eventoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));

        eventoAtual.setNome(eventoNovo.getNome());
        eventoAtual.setDataInicial(eventoNovo.getDataInicial());
        eventoAtual.setDataFim(eventoNovo.getDataFim());

        return this.salvar(eventoAtual);
    }

    @Override
    public List<Evento> listar(String nome, LocalDate dataInicial, LocalDate dataFim) {
        return eventoRepository.procurarPorFiltro(nome, dataInicial, dataFim);
    }

    @Override
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MSG_NOT_FOUND));
    }
}
