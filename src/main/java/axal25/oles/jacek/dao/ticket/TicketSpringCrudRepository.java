package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository("SpringCrudRepository")
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

    @Override
    default void updateTicket(TicketEntity ticket) {

    }

    @Override
    default void closeTicketById(int ticketId) {

    }

    @Override
    default void deleteTicketById(int ticketId) {
        deleteById(ticketId);
    }
}
