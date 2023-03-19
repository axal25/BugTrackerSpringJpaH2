package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcTicketDaoTest {

    private JdbcTicketDao ticketDao;

    @BeforeEach
    void setUp() throws SQLException {
        ticketDao = new JdbcTicketDao();
    }

    @Test
    public void insertTicket_isInsertedSuccessfully() throws SQLException {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "insertTicket_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                null);

        insertTicketAndAssert(inputTicket);
    }

    @Test
    public void selectTickets() throws SQLException {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "selectTickets",
                getClass(),
                null,
                null,
                null);

        insertTicketAndAssert(inputTicket);

        List<TicketEntity> selecteds = ticketDao.selectTickets();

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputTicket));
    }

    public void insertTicketAndAssert(TicketEntity inputTicket) throws SQLException {
        Optional<TicketEntity> inserted = ticketDao.insertTicket(inputTicket);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputTicket);
    }
}
