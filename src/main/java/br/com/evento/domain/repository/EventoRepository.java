package br.com.evento.domain.repository;

import br.com.evento.domain.model.Evento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EventoRepository extends SimpleJpaRepository<Evento, Long> {

    @PersistenceContext
    private EntityManager em;

    public EventoRepository(EntityManager em) {
        super(Evento.class, em);
        this.em = em;
    }

    //TODO: nome as vezes vem como "", hotfix da condicional para verificar se não é vazio ou nunlo
    public List<Evento> procurarPorFiltro(String nome, LocalDate dataInicial, LocalDate dataFim) {
        StringBuilder qry = new StringBuilder();

        qry.append("FROM Evento e ");

        if (nome != null && !nome.isEmpty() || dataInicial != null || dataFim != null)
            qry.append("WHERE ");

        if (nome != null && !nome.isEmpty()) {
            qry.append("e.nome LIKE :nome ");
        }

        if (dataInicial != null && dataFim == null) {
            if (nome != null && !nome.isEmpty())
                qry.append("AND ");

            qry.append("e.dataInicial >= :dataInicial ");
        }

        if (dataFim != null && dataInicial == null) {
            if (dataInicial != null || nome != null && !nome.isEmpty())
                qry.append("AND ");

            qry.append("e.dataFim <= :dataFim ");
        }

        if (dataInicial != null && dataFim != null) {
            if (nome != null && !nome.isEmpty())
                qry.append("AND ");

            qry.append("e.dataInicio between :dataInicio ");
        }

        var qryResult = em.createQuery(qry.toString(), Evento.class);

        if (nome != null && !nome.isEmpty()) {
            qryResult.setParameter("nome", '%' + nome + '%');
        }

        if (dataInicial != null && dataFim == null) {
            qryResult.setParameter("dataInicial", dataInicial);
        }

        if (dataFim != null && dataInicial == null) {
            qryResult.setParameter("dataFim", dataFim);
        }

        return qryResult.getResultList();
    }
}
