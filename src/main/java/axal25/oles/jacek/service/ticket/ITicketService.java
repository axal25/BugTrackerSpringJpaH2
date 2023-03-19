package axal25.oles.jacek.service.ticket;

import axal25.oles.jacek.entity.TicketEntity;

import java.util.List;

public interface ITicketService {
    List<TicketEntity> getAllTickets();

    TicketEntity getTicketById(int ticketId);

    void addTicket(TicketEntity ticket);

    void updateTicket(TicketEntity ticket);

    void closeTicket(int ticketId);

    void deleteTicket(int ticketId);
}
