package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class TickerCrudRepositoryTest {

    @Autowired
    @Qualifier("SpringCrudRepository")
    private ITicketDao ticketDao;

    @Test
    public void addTicket_successful() {
        TicketEntity ticket = TicketEntityFactory.produce(
                "addTicket_successful",
                TickerCrudRepositoryTest.class,
                null,
                null,
                null);
        ticket.setId(null);
        ticket.getApplication().setId(null);
        ticket.getRelease().setId(null);

        ticketDao.addTicket(ticket);

        assertThat(ticket.getId()).isNotNull();
        assertThat(ticket.getApplication().getId()).isNotNull();
        assertThat(ticket.getRelease().getId()).isNotNull();
        TicketEntity retrievedTicket = ticketDao.getTicketById(1);
        assertThat(ticket).isEqualTo(retrievedTicket);
    }

    @Test
    public void getTicketById_successful() {
        ticketDao.getTicketById(1);
    }
}
