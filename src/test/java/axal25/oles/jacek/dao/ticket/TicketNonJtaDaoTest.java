package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.dao.DaoTestHelper;
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

    @Autowired
    private DaoTestHelper daoTestHelper;

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
            public DaoTestHelper getDaoTestHelper() {
                return daoTestHelper;
            }
        };
    }

    @Test
    public void updateTicket() {
        tester.updateTicket();
    }

    @Test
    public void closeTicket() {
        tester.closeTicket();
    }

    @Test
    public void deleteTicket_deletesAddedTicket() {
        tester.deleteTicket_deletesAddedTicket();
    }

    @Test
    public void getAllTickets_containsAddedTicket() {
        tester.getAllTickets_containsAddedTicket();
    }

    @Test
    public void getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities() {
        tester.getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities();
    }

    @Test
    public void getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities() {
        tester.getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities();
    }

    @Test
    public void addTicket_isSuccessfullyAdded() {
        tester.addTicket_isSuccessfullyAdded();
    }
}
