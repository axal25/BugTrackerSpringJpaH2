package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

public class JdbcReleaseToTicketDaoTest {

    private JdbcReleaseToTicketDao releaseToTicketDao;
    private JdbcReleaseDaoTest releaseDaoTest;
    private JdbcTicketDaoTest ticketDaoTest;

    @BeforeEach
    void setUp() throws SQLException {
        releaseToTicketDao = new JdbcReleaseToTicketDao();
        releaseDaoTest = new JdbcReleaseDaoTest();
        releaseDaoTest.setUp();
        ticketDaoTest = new JdbcTicketDaoTest();
        ticketDaoTest.setUp();
    }

    @Test
    public void insertReleaseIdToTicketId_isInsertedSuccessfully() throws SQLException {
        String methodName = "insertReleaseIdToTicketId_isInsertedSuccessfully";

        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null);
        inputRelease.setApplications(List.of());

        TicketEntity inputTicket = TicketEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                null);
        inputTicket.setRelease(null);

        insertReleaseIdToTicketIdAndAssert(inputRelease, inputTicket);
    }

    @Test
    public void selectReleaseIdToTicketIdMap() throws SQLException {
        String methodName = "selectReleaseIdToTicketIdMap";

        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null);
        inputRelease.setApplications(List.of());

        TicketEntity inputTicket = TicketEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                null);
        inputTicket.setRelease(null);

        insertReleaseIdToTicketIdAndAssert(inputRelease, inputTicket);

        Map<Integer, Integer> selectedReleaseIdToTicketIdMap = releaseToTicketDao.selectReleaseIdToTicketIdMap();

        assertThat(selectedReleaseIdToTicketIdMap)
                .containsAtLeast(inputRelease.getId(), inputTicket.getId());
    }

    private void insertReleaseIdToTicketIdAndAssert(ReleaseEntity inputRelease, TicketEntity inputTicket) throws SQLException {

        releaseDaoTest.insertReleaseAndAssert(inputRelease);
        ticketDaoTest.insertTicketAndAssert(inputTicket);

        Optional<AbstractMap.SimpleEntry<Integer, Integer>> insertedReleaseIdToTicketId =
                releaseToTicketDao.insertReleaseIdToTicketId(inputRelease.getId(), inputTicket.getId());

        assertThat(insertedReleaseIdToTicketId).isNotNull();
        assertThat(insertedReleaseIdToTicketId.isPresent()).isTrue();
        assertThat(insertedReleaseIdToTicketId.get().getKey()).isEqualTo(inputRelease.getId());
        assertThat(insertedReleaseIdToTicketId.get().getValue()).isEqualTo(inputTicket.getId());
    }
}
