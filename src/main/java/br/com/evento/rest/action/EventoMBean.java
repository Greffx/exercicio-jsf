package br.com.evento.rest.action;

import br.com.evento.domain.dto.EventoDTO;
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
    private List<EventoDTO> eventosDto;

    @PostConstruct
    public void init() {
        var facesContext = FacesContext.getCurrentInstance();
        var idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");

        if (idParam != null) {
            var id = Long.valueOf(idParam);
            evento = eventoService.buscarPorId(id);
        } else {
            evento = new Evento();
        }
        eventosDto = EventoDTO.toDTO(eventoService.listar(null, null, null));
    }

    public void salvar() {
        try {
            if (evento.getId() == null) {
                evento = eventoService.salvar(evento);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento cadastrado com sucesso!"));
            } else {
                evento = eventoService.editar(evento.getId(), evento);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento atualizado com sucesso!"));
            }
        } catch (BusinessException exc) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", exc.getMessage()));
        }
    }

    public void listar() {
        eventosDto = EventoDTO.toDTO(eventoService.listar(evento.getNome(), evento.getDataInicial(), evento.getDataFim()));
    }

    public void deletar(Long id) {
        eventoService.excluir(id);
        listar();
    }

}
