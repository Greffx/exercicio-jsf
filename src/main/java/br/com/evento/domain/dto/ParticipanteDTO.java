package br.com.evento.domain.dto;

import br.com.evento.domain.model.Participante;
import br.com.evento.domain.model.Presenca;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipanteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8257093019384834909L;

    private Long id;
    private Long eventoId;
    private Long presencaId;

    private String nome;
    private String cpf;
    private String email;

    private Integer porcentagem;

    public static List<ParticipanteDTO> toDTO(List<Participante> participantes) {
        var participanteDTOList = new ArrayList<ParticipanteDTO>();

        for (var participante : participantes) {
            var participanteDTO = new ParticipanteDTO();

            participanteDTO.setId(participante.getId());
            participanteDTO.setEventoId(participante.getEventoId());
            participanteDTO.setNome(participante.getNome());
            participanteDTO.setCpf(participante.getCpf());
            participanteDTO.setEmail(participante.getEmail());

            if (participante.getPresencas() != null && !participante.getPresencas().isEmpty()) {
                for (var presenca : participante.getPresencas()) {
                    if (presenca.getEventoId().equals(participante.getEventoId())) {
                        participanteDTO.setPorcentagem(presenca.getPorcentagem());
                        participanteDTO.setPresencaId(presenca.getId());

                        //só tem uma presenca daquele participante naquele evento
                        break;
                    }
                }
            }

            participanteDTOList.add(participanteDTO);
        }

        return participanteDTOList;
    }
}
