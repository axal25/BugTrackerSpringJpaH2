package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("springCrudRepository")
public interface TicketSpringCrudRepository extends CrudRepository<TicketEntity, Integer>, ITicketDao {

    @Override
    default List<TicketEntity> getAllTickets() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    default TicketEntity addTicket(TicketEntity ticket) {
        return save(ticket);
    }

    @Override
    default TicketEntity getTicketById(int ticketId) {
        return findById(ticketId).orElse(null);
    }

    @Transactional
    @Override
    default void updateTicket(TicketEntity updated) {
        TicketEntity existing = getTicketById(updated.getId());
        existing.setDescription(updated.getDescription());
        existing.setApplication(updated.getApplication());
        existing.setTitle(updated.getTitle());

    }

    @Transactional
    @Override
    default void closeTicketById(int ticketId) {
        TicketEntity existing = getTicketById(ticketId);
        existing.setStatus("Resolved");
    }

    @Override
    default void deleteTicketById(int ticketId) {
        deleteById(ticketId);
    }
}
