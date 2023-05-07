package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JTA = Java Transaction API
 */
@Transactional
@Repository("Jta")
public class TicketJtaDao implements ITicketDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TicketEntity> getAllTickets() {
        String jpqlQuery = "SELECT ticket FROM " +
                TicketEntity.class.getSimpleName() +
                " ticket ORDER BY ticket.id";
        List<?> resultSet = entityManager.createQuery(jpqlQuery).getResultList();
        return resultSet.stream()
                .map(ticket -> (TicketEntity) ticket)
                .collect(Collectors.toList());
    }

    @Override
    public TicketEntity addTicket(TicketEntity ticket) {
        entityManager.persist(ticket);
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
    }

    @Override
    public void deleteTicketById(int ticketId) {
        entityManager.remove(ticketId);
    }
}
