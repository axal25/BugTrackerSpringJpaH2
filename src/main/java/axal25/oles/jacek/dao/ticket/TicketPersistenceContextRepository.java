package axal25.oles.jacek.dao.ticket;

import javax.persistence.EntityManager;

public interface TicketPersistenceContextRepository {
    EntityManager getEntityManager();
}
