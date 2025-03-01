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
    private List<ParticipanteDTO> participantesDto = new ArrayList<>();
    private Long eventoId;
    private ParticipanteDTO participanteDto;

    @PostConstruct
    public void init() {
        var facesContext = FacesContext.getCurrentInstance();
        participante = new Participante();
        eventoId = Long.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("id"));
        participantesDto = ParticipanteDTO.toDTO(participanteService.listar(eventoId));

        if (participantesDto == null || participantesDto.isEmpty())
            participantesDto.add(participanteDto);

    }

    public void salvar() {
        try {
            if (participante.getId() == null) {
                participante = participanteService.salvar(eventoId, participante);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento cadastrado com sucesso!"));
            } else {
                participante = participanteService.editar(participante.getId(), participante);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
                                "Evento atualizado com sucesso!"));
            }
        } catch (BusinessException exc) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", exc.getMessage()));
        }

    }

    public void listar(Long eventoId) {
        participantesDto = ParticipanteDTO.toDTO(participanteService.listar(eventoId));
    }

}
