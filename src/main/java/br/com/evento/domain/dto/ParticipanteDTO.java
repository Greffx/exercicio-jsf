package br.com.evento.domain.dto;

import br.com.evento.domain.model.Participante;
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

    private String nome;
    private String cpf;
    private String email;
    private String porcentagem;

    public static List<ParticipanteDTO> toDTO(List<Participante> participantes) {
        var participanteDTOList = new ArrayList<ParticipanteDTO>();

        for (var participante : participantes) {
            var participanteDTO = new ParticipanteDTO();

            participanteDTO.setId(participante.getId());
            participanteDTO.setEventoId(participante.getEventoId());
            participanteDTO.setNome(participante.getNome());
            participanteDTO.setCpf(participante.getCpf());
            participanteDTO.setEmail(participante.getEmail());

            participanteDTOList.add(participanteDTO);
        }

        return participanteDTOList;
    }
}
