package br.com.evento.rest.action;

import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Evento;
import br.com.evento.domain.service.EventoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ViewScoped
@Component(value = "eventoMBean")
public class EventoMBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -7477193145612331051L;

    @Autowired
    private EventoService eventoService;

    private Evento evento;
    private List<Evento> eventos;
    private String nome;
    private LocalDate dataInicial;
    private LocalDate dataFim;

    @PostConstruct
    public void init() {
        evento = new Evento();
        eventos = eventoService.listar( null, null, null);
    }

    public String salvar() {
        try {
            if (evento.getId() == null) {
                eventoService.salvar(evento);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento cadastrado com sucesso!"));
            } else {
                eventoService.editar(evento.getId());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento atualizado com sucesso!"));
            }

            return null;
        } catch (BusinessException exc) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", exc.getMessage()));

            return null;
        }
    }

    public void listar() {
        eventos = eventoService.listar(nome, dataInicial, dataFim);
    }

}
