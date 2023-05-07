package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcReleaseToTicketDaoTest {
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseUtils.getConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
    }

    @Test
    public void insertReleaseIdToTicketId_isInsertedSuccessfully() throws SQLException {
        String methodName = "insertReleaseIdToTicketId_isInsertedSuccessfully";

        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);
        inputRelease.setApplications(List.of());

        TicketEntity inputTicket = TicketEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);
        inputTicket.setRelease(null);

        insertReleaseIdToTicketIdAndAssert(inputRelease, inputTicket, connection);
    }

    @Test
    public void selectReleaseIdToTicketIdMap() throws SQLException {
        String methodName = "selectReleaseIdToTicketIdMap";

        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);
        inputRelease.setApplications(List.of());

        TicketEntity inputTicket = TicketEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);
        inputTicket.setRelease(null);

        insertReleaseIdToTicketIdAndAssert(inputRelease, inputTicket, connection);

        Map<Integer, Integer> selectedReleaseIdToTicketIdMap =
                JdbcReleaseToTicketDao.selectReleaseIdToTicketIdMap(connection);

        assertThat(selectedReleaseIdToTicketIdMap)
                .containsAtLeast(inputRelease.getId(), inputTicket.getId());
    }

    private void insertReleaseIdToTicketIdAndAssert(ReleaseEntity inputRelease, TicketEntity inputTicket, Connection connection) throws SQLException {

        JdbcReleaseDao.insertRelease(inputRelease, connection);
        JdbcTicketDao.insertTicket(inputTicket, connection);

        Optional<AbstractMap.SimpleEntry<Integer, Integer>> insertedReleaseIdToTicketId =
                JdbcReleaseToTicketDao.insertReleaseIdToTicketId(
                        inputRelease.getId(),
                        inputTicket.getId(),
                        connection);

        assertThat(insertedReleaseIdToTicketId).isNotNull();
        assertThat(insertedReleaseIdToTicketId.isPresent()).isTrue();
        assertThat(insertedReleaseIdToTicketId.get().getKey()).isEqualTo(inputRelease.getId());
        assertThat(insertedReleaseIdToTicketId.get().getValue()).isEqualTo(inputTicket.getId());
    }
}
