package br.com.evento.rest.action;

import br.com.evento.domain.dto.ParticipanteDTO;
import br.com.evento.domain.exception.BusinessException;
import br.com.evento.domain.model.Participante;
import br.com.evento.domain.model.Presenca;
import br.com.evento.domain.service.ParticipanteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
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

    private Presenca presenca;
    private Participante participante;
    private ParticipanteDTO participanteDto;

    private List<ParticipanteDTO> participantesDto;
    private List<Presenca> presencas;

    private Long eventoId;
    private Long presencaId;
    private Long participanteId;

    @PostConstruct
    public void init() {
        var facesContext = FacesContext.getCurrentInstance();

        participante = new Participante();
        presenca = new Presenca();

        var ParamEventoId = facesContext.getExternalContext().getRequestParameterMap().get("idEvento");
        var ParamParticipanteId = facesContext.getExternalContext().getRequestParameterMap().get("idParticipante");
        var ParamPresencaId = facesContext.getExternalContext().getRequestParameterMap().get("idPresenca");

        if (ParamEventoId != null)
            eventoId = Long.valueOf(ParamEventoId);

        if (ParamParticipanteId != null)
            participanteId = Long.valueOf(ParamParticipanteId);

        if (ParamPresencaId != null)
            presencaId = Long.valueOf(ParamPresencaId);

        participantesDto = ParticipanteDTO.toDTO(participanteService.listar(eventoId));
    }

    public void salvar() {
        try {
            if (participante.getId() == null) {
                participante = participanteService.salvar(eventoId, participante);
                this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso",
                        "Participante criado com sucesso!");
            } else {
                participante = participanteService.editar(eventoId, participante.getId(), participante);
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

    public void buscarPorId() {
        participante = participanteService.findParticipanteByEventoId(eventoId, participanteId);
    }

    public void deletar(Long id) {
        participanteService.excluir(id);
        listar(eventoId);
        this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Confirmado", "Excluído com sucesso");
    }

    public void adicionarPresenca() {
        try {
            participanteService.salvarPresenca(eventoId, participanteId, presenca, false);
            this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Confirmado", "Presença confirmada!");
            PrimeFaces.current().executeScript(
                    "PF('presencaDialog').hide(); PF('tabViewWidget').select(1);");
            listar(eventoId);
        } catch (BusinessException exc) {
            this.adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Error", exc.getMessage());
        }
    }

    public void deletarPresenca(long idPresenca) {
        participanteService.excluirPresenca(idPresenca);

        //necessário restar para que não quebre com o JPA
        presenca = new Presenca();
        presencaId = null;

        listar(eventoId);
        this.adicionarMensagem(FacesMessage.SEVERITY_INFO, "Confirmado", "Presença excluida!");
    }

    public void adicionarMensagem(FacesMessage.Severity severity, String titulo, String detalhe) {
        FacesMessage message = new FacesMessage(severity, titulo, detalhe);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
