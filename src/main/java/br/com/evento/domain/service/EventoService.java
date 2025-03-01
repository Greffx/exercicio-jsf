package br.com.evento.domain.service;

import br.com.evento.domain.model.Evento;

import java.time.LocalDate;
import java.util.List;

public interface EventoService {

    Evento salvar(Evento evento);

    boolean excluir(Long id);

    Evento editar(Long id);

    List<Evento> listar(String nome, LocalDate dataInicio, LocalDate dataFim);

    Evento buscarPorId(Long id);

}
