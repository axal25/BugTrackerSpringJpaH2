package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;

import java.util.List;

public interface ITicketDao {
    List<TicketEntity> getAllTickets();

    TicketEntity addTicket(TicketEntity ticket);

    TicketEntity getTicketById(int ticketId);

    void updateTicket(TicketEntity ticket);

    void closeTicketById(int ticketId);

    void deleteTicketById(int ticketId);
}
