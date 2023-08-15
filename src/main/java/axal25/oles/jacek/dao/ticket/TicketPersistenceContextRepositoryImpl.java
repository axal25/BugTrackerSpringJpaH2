package axal25.oles.jacek.dao.ticket;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

public class TicketPersistenceContextRepositoryImpl implements TicketPersistenceContextRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(entityManager);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
