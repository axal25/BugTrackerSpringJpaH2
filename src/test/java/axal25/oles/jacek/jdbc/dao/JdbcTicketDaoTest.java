package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcTicketDaoTest {
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseUtils.getConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
    }

    @Test
    public void insertTicket_isInsertedSuccessfully() throws SQLException {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "insertTicket_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertTicketAndAssert(inputTicket);
    }

    @Test
    public void selectTickets() throws SQLException {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "selectTickets",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertTicketAndAssert(inputTicket);

        List<TicketEntity> selecteds = JdbcTicketDao.selectTickets(connection);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputTicket));
    }

    @Test
    public void selectTicketIds() throws Exception {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "selectTicketIds",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertTicketAndAssert(inputTicket);

        List<Integer> selecteds = JdbcTicketDao.selectTicketIds(connection);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputTicket.getId()));
    }

    private void insertTicketAndAssert(TicketEntity inputTicket) throws SQLException {
        Optional<TicketEntity> inserted = JdbcTicketDao.insertTicket(inputTicket, connection);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputTicket);
    }
}
