package br.com.evento.domain.repository;

import br.com.evento.domain.model.Presenca;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PresencaRepository extends SimpleJpaRepository<Presenca, Long> {

    @PersistenceContext
    private EntityManager em;

    public PresencaRepository(EntityManager em) {
        super(Presenca.class, em);
        this.em = em;
    }

    public List<Presenca> findTodosByEvento(Long eventoId) {
        var qry = new StringBuilder();

        qry.append("FROM Presenca p ");
        qry.append("JOIN FETCH evento e ");
        qry.append("JOIN FETCH participante pa ");
        qry.append("WHERE p.evento.id = :eventoId ");

        var qryResultado = em.createQuery(qry.toString(), Presenca.class);
        qryResultado.setParameter("eventoId", eventoId);

        return qryResultado.getResultList();
    }

    public List<Presenca> findTodosByParticipante(Long participanteId) {
        var qry = new StringBuilder();

        qry.append("FROM Presenca p ");
        qry.append("JOIN FETCH evento e ");
        qry.append("JOIN FETCH participante pa ");
        qry.append("WHERE p.participante.id = :participanteId ");

        var qryResultado = em.createQuery(qry.toString(), Presenca.class);
        qryResultado.setParameter("participanteId", participanteId);

        return qryResultado.getResultList();
    }
}
