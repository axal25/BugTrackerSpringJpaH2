package axal25.oles.jacek.service.ticket;

import axal25.oles.jacek.dao.ticket.ITicketDao;
import axal25.oles.jacek.entity.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService implements ITicketService {

    @Autowired
    @Qualifier("Jta")
    private ITicketDao ticketDao;
    
    @Override
    public List<TicketEntity> getAllTickets() {
        return ticketDao.getAllTickets();
    }

    @Override
    public TicketEntity getTicketById(int ticketId) {
        return ticketDao.getTicketById(ticketId);
    }

    @Override
    public void addTicket(TicketEntity ticket) {
        ticketDao.addTicket(ticket);
    }

    @Override
    public void updateTicket(TicketEntity ticket) {
        ticketDao.updateTicket(ticket);
    }

    @Override
    public void closeTicket(int ticketId) {
        ticketDao.closeTicketById(ticketId);
    }

    @Override
    public void deleteTicket(int ticketId) {
        ticketDao.deleteTicketById(ticketId);
    }
}
