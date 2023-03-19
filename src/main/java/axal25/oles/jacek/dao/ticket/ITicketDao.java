package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;

import java.util.List;

public interface ITicketDao {
    List<TicketEntity> getAllTickets();

    void addTicket(TicketEntity ticket);

    TicketEntity getTicketById(int ticketId);

    void updateTicket(TicketEntity ticket);

    void closeTicket(int ticketId);

    void deleteTicket(int ticketId);
}
