package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.dao.DaoTestHelper;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class TickerCrudRepositoryTest {

    @Autowired
    @Qualifier("SpringCrudRepository")
    private ITicketDao ticketDao;

    @Autowired
    private DaoTestHelper daoTestHelper;

    @Test
    public void addTicket_isEqual_skippingLazyFetchedEntities() {
        TicketEntity createTicket = TicketEntityFactory.produce(
                "addTicket_isEqual_skippingLazyFetchedEntities",
                TickerCrudRepositoryTest.class,
                null,
                null,
                null);
        createTicket.setId(null);
        createTicket.getApplication().setId(null);
        createTicket.getRelease().setId(null);

        ticketDao.addTicket(createTicket);

        assertThat(createTicket.getId()).isNotNull();
        assertThat(createTicket.getApplication().getId()).isNotNull();
        assertThat(createTicket.getRelease().getId()).isNotNull();
        TicketEntity retrievedTicket = ticketDao.getTicketById(createTicket.getId());
        createTicket.getRelease().setApplications(null);
        retrievedTicket.getRelease().setApplications(null);
        assertThat(createTicket).isEqualTo(retrievedTicket);
    }

    @Test
    public void addTicket_IsEqual_includingLazyFetchedEntities() {
        TicketEntity createTicket = TicketEntityFactory.produce(
                "addTicket_IsEqual_includingLazyFetchedEntities",
                TickerCrudRepositoryTest.class,
                null,
                null,
                null);
        createTicket.setId(null);
        createTicket.getApplication().setId(null);
        createTicket.getRelease().setId(null);

        AtomicReference<TicketEntity> retrievedTicket = new AtomicReference<>();

        daoTestHelper.wrapInTransaction(entityManager -> {
            ticketDao.addTicket(createTicket);
            retrievedTicket.set(ticketDao.getTicketById(createTicket.getId()));
            assertThat(retrievedTicket.get().getRelease().getApplications().get(0)).isEqualTo(createTicket.getApplication());
        });

        assertThat(createTicket.getId()).isNotNull();
        assertThat(createTicket.getApplication().getId()).isNotNull();
        assertThat(createTicket.getRelease().getId()).isNotNull();

        assertThat(createTicket).isEqualTo(retrievedTicket.get());
    }

    @Test
    public void getTicketById_successful() {
        ticketDao.getTicketById(1);
        // TODO: finish
    }
}
