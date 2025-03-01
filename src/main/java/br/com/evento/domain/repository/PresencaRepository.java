package br.com.evento.domain.repository;

import br.com.evento.domain.model.Presenca;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PresencaRepository extends SimpleJpaRepository<Presenca, Long> {

    @PersistenceContext
    private EntityManager em;

    public PresencaRepository(EntityManager em) {
        super(Presenca.class, em);
        this.em = em;
    }
}
