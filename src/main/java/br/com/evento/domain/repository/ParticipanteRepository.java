package br.com.evento.domain.repository;

import br.com.evento.domain.model.Participante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipanteRepository extends SimpleJpaRepository<Participante, Long> {

    @PersistenceContext
    private EntityManager em;

    public ParticipanteRepository(EntityManager em) {
        super(Participante.class, em);
        this.em = em;
    }
}
