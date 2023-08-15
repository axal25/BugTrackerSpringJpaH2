package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("crudRepository")
public interface TicketCrudRepository extends
        CrudRepository<TicketEntity, Integer>,
        ITicketDao,
        TicketPersistenceContextRepository {

    @Override
    default List<TicketEntity> getAllTickets() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @Query("SELECT DISTINCT ticket from TicketEntity ticket" +
            " LEFT JOIN FETCH ticket.release release" +
            " LEFT JOIN FETCH release.applications apps" +
            " ORDER BY ticket.id")
    List<TicketEntity> getAllTicketsEagerly();

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
    default TicketEntity getTicketByIdEagerly(int ticketId) {
        Optional<TicketEntity> optionalTicket = findById(ticketId);
        optionalTicket.ifPresent(ticket -> {
            int unusedToFetchEagerly = ticket.getRelease().getApplications().size();
        });
        return optionalTicket.orElse(null);
    }


    @Transactional
    @Override
    default void updateTicket(TicketEntity updated) {
        TicketEntity existing = getTicketById(updated.getId());
        existing.setDescription(updated.getDescription());
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
