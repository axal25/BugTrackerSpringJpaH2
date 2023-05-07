package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;
// import javax.transaction.Transactional; // should not be present

/**
 * JTA = Java Transaction API
 */
// @Transactional // should not be present to be non-JTA
@Repository("nonJta")
public class TicketNonJtaDao implements ITicketDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TicketEntity> getAllTickets() {
        String jpqlQuery = "SELECT ticket.* FROM " +
                TicketEntity.class.getSimpleName() +
                " ticket ORDER BY ticket.id";
        List<?> resultSet = entityManager.createQuery(jpqlQuery).getResultList();
        return resultSet.stream()
                .map(ticket -> (TicketEntity) ticket)
                .collect(Collectors.toList());
    }

    @Override
    public TicketEntity addTicket(TicketEntity ticket) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try {
            entityManager.persist(ticket);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }

        return ticket;
    }

    @Override
    public TicketEntity getTicketById(int ticketId) {
        return null;
    }

    @Override
    public void updateTicket(TicketEntity updated) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        TicketEntity existing = getTicketById(updated.getId());

        existing.setDescription(updated.getDescription());
        existing.setApplication(updated.getApplication());
        existing.setTitle(updated.getTitle());

        try {
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }
    }

    @Override
    public void closeTicketById(int ticketId) {

    }

    @Override
    public void deleteTicketById(int ticketId) {

    }
}
