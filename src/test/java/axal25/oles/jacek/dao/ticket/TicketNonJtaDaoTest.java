package axal25.oles.jacek.dao.ticket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class TicketNonJtaDaoTest {

    @Autowired
    @Qualifier("nonJtaDao")
    private ITicketDao ticketDao;

    private ITicketDaoTester tester;

    @BeforeAll
    static void beforeAll() {
        ITicketDaoTester.validateAllTestMethodsAreImplementedAndAnnotated(TicketNonJtaDaoTest.class);
    }

    @BeforeEach
    void setUp() {
        tester = new ITicketDaoTester() {
            @Override
            public ITicketDao getTicketDao() {
                return ticketDao;
            }

            @Override
            public Class<?> getTestClass() {
                return TicketNonJtaDaoTest.class;
            }
        };
    }

    @Test
    public void updateTicket_updatesDescriptionAndTitle() {
        tester.updateTicket_updatesDescriptionAndTitle();
    }

    @Test
    public void closeTicket_updatesStatusToResolvedValue() {
        tester.closeTicket_updatesStatusToResolvedValue();
    }

    @Test
    public void deleteTicket_deletesAddedTicket() {
        tester.deleteTicket_deletesAddedTicket();
    }

    @Test
    public void getAllTickets_containsAddedTicket_skippingLazilyFetchedEntities() {
        tester.getAllTickets_containsAddedTicket_skippingLazilyFetchedEntities();
    }

    @Test
    public void getAllTicketsEagerly_containsAddedTicket_includingLazilyFetchedEntities() {
        tester.getAllTicketsEagerly_containsAddedTicket_includingLazilyFetchedEntities();
    }

    @Test
    public void getTicketByIdEagerly_IsEqualToAddedTicket_includingLazilyFetchedEntities() {
        tester.getTicketByIdEagerly_IsEqualToAddedTicket_includingLazilyFetchedEntities();
    }

    @Test
    public void getTicketById_isEqualToAddedTicket_skippingLazilyFetchedEntities() {
        tester.getTicketById_isEqualToAddedTicket_skippingLazilyFetchedEntities();
    }

    @Test
    public void addTicket_isSuccessfullyAdded() {
        tester.addTicket_isSuccessfullyAdded();
    }
}
