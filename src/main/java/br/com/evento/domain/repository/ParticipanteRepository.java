package br.com.evento.domain.repository;

import br.com.evento.domain.model.Participante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParticipanteRepository extends SimpleJpaRepository<Participante, Long> {

    @PersistenceContext
    private EntityManager em;

    public ParticipanteRepository(EntityManager em) {
        super(Participante.class, em);
        this.em = em;
    }

    public List<Participante> findByEvento(Long eventoId) {
        var qry = new StringBuilder();

        qry.append("FROM Participante p ");
        qry.append("WHERE p.eventoId = :eventoId ");

        var qryResult = em.createQuery(qry.toString(), Participante.class);
        qryResult.setParameter("eventoId", eventoId);

        return qryResult.getResultList();
    }


    public Participante findParticipanteByEventoId(Long eventoId, Long participanteId) {
        var qry = new StringBuilder();

        qry.append("FROM Participante p ");
        qry.append("LEFT JOIN FETCH p.evento ");
        qry.append("WHERE p.evento.id = :eventoId ");
        qry.append("AND p.id = :participanteId ");

        var qryResult = em.createQuery(qry.toString(), Participante.class);
        qryResult.setParameter("eventoId", eventoId);
        qryResult.setParameter("participanteId", participanteId);

        return qryResult.getSingleResult();
    }

    public List<Participante> findByCpf(String cpf) {
        var qry = new StringBuilder();

        qry.append("FROM Participante p ");
        qry.append("WHERE p.cpf = :cpf ");

        var qryResult = em.createQuery(qry.toString(), Participante.class);
        qryResult.setParameter("cpf", cpf);

        return qryResult.getResultList();
    }
}
