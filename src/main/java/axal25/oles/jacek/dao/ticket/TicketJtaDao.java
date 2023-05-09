package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * JTA = Java Transaction API
 */
@Transactional
@Repository("jtaDao")
public class TicketJtaDao implements ITicketDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TicketEntity> getAllTickets() {
        String jpqlQuery = "SELECT ticket from " +
                TicketEntity.class.getSimpleName() +
                " ticket ORDER BY ticket.id";
        return entityManager.createQuery(jpqlQuery, TicketEntity.class).getResultList();
    }

    @Override
    public TicketEntity addTicket(TicketEntity ticket) {
        entityManager.persist(ticket);
        entityManager.flush();
        return ticket;
    }

    @Override
    public TicketEntity getTicketById(int ticketId) {
        return entityManager.find(TicketEntity.class, ticketId);
    }

    @Override
    public void updateTicket(TicketEntity updated) {
        TicketEntity existing = getTicketById(updated.getId());
        existing.setDescription(updated.getDescription());
        existing.setApplication(updated.getApplication());
        existing.setTitle(updated.getTitle());
        entityManager.flush();
    }

    @Override
    public void closeTicketById(int ticketId) {
        TicketEntity ticket = getTicketById(ticketId);
        ticket.setStatus("Resolved");
        entityManager.flush();
    }

    @Override
    public void deleteTicketById(int ticketId) {
        entityManager.remove(getTicketById(ticketId));
        entityManager.flush();
    }
}
