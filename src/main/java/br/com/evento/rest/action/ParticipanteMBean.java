package br.com.evento.rest.action;

import br.com.evento.domain.dto.ParticipanteDTO;
import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Participante;
import br.com.evento.domain.service.ParticipanteService;
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
import java.util.ArrayList;
import java.util.List;

//TODO: depois pegar o context do JSF e deiaxr msg generics, com method justamente pra isso
@Getter
@Setter
@ViewScoped
@Component(value = "participanteMBean")
public class ParticipanteMBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -4402994640565533260L;

    @Autowired
    private ParticipanteService participanteService;

    private Participante participante;
    private List<ParticipanteDTO> participantesDto;
    private Long eventoId;
    private ParticipanteDTO participanteDto;

    @PostConstruct
    public void init() {
        var facesContext = FacesContext.getCurrentInstance();
        participante = new Participante();
        var idParam = facesContext.getExternalContext().getRequestParameterMap().get("id");

        if (idParam != null)
            eventoId = Long.valueOf(idParam);

        participantesDto = ParticipanteDTO.toDTO(participanteService.listar(eventoId));
    }

    public void salvar() {
        try {
            if (participante.getId() == null) {
                participante = participanteService.salvar(eventoId, participante);
                this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso",
                        "Participante criado com sucesso!");
            } else {
                participante = participanteService.editar(participante.getId(), participante);
                this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso",
                        "Participante atualizado com sucesso!");
            }
        } catch (BusinessException exc) {
            this.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Error",
                    exc.getMessage());
        }

        listar(eventoId);
    }

    public void listar(Long eventoId) {
        participantesDto = ParticipanteDTO.toDTO(participanteService.listar(eventoId));
    }

    public void deletar(Long id) {
        participanteService.excluir(id);
        listar(eventoId);
        this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Confirmado", "Excluído com sucesso");
    }

    public void adicionarMensagem(FacesMessage.Severity severity, String titulo, String detalhe) {
        FacesMessage message = new FacesMessage(severity, titulo, detalhe);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
