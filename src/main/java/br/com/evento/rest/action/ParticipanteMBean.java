package br.com.evento.rest.action;

import br.com.evento.domain.service.ParticipanteService;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ViewScoped
@Component(value = "participanteMBean")
public class ParticipanteMBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -4402994640565533260L;

    @Autowired
    private ParticipanteService participanteService;





}
