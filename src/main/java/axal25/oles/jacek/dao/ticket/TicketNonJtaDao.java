package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.util.List;
// import javax.transaction.Transactional; // should not be present

/**
 * JTA = Java Transaction API
 */
// @Transactional // should not be present to be non-JTA
@Repository("nonJtaDao")
public class TicketNonJtaDao implements ITicketDao {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @PostConstruct
    public void postConstruct() {
        entityManager = entityManagerFactory.createEntityManager();
    }


    @Override
    public List<TicketEntity> getAllTickets() {
        String jpqlQuery = "SELECT DISTINCT ticket FROM " +
                TicketEntity.class.getSimpleName() +
                " ticket ORDER BY ticket.id";
        return entityManager.createQuery(jpqlQuery, TicketEntity.class).getResultList();
    }

    @Override
    public List<TicketEntity> getAllTicketsEagerly() {
        String jpqlQuery = "SELECT DISTINCT ticket from " +
                TicketEntity.class.getSimpleName() +
                " ticket LEFT JOIN FETCH ticket.release release" +
                " LEFT JOIN FETCH release.applications apps" +
                " ORDER BY ticket.id";
        return entityManager.createQuery(jpqlQuery, TicketEntity.class).getResultList();
    }

    @Override
    public TicketEntity addTicket(TicketEntity ticket) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.persist(ticket);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }

        return ticket;
    }

    @Override
    public TicketEntity getTicketById(int ticketId) {
        return entityManager.find(TicketEntity.class, ticketId);
    }

    @Override
    public TicketEntity getTicketByIdEagerly(int ticketId) {
        TicketEntity ticket = entityManager.find(TicketEntity.class, ticketId);
        int unusedToFetchEagerly = ticket.getRelease().getApplications().size();
        return ticket;
    }

    @Override
    public void updateTicket(TicketEntity updated) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        TicketEntity existing = getTicketById(updated.getId());
        existing.setDescription(updated.getDescription());
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
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        TicketEntity existing = getTicketById(ticketId);
        existing.setStatus("Resolved");

        try {
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }
    }

    @Override
    public void deleteTicketById(int ticketId) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        TicketEntity existing = getTicketById(ticketId);
        entityManager.remove(existing);

        try {
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }
    }
}
