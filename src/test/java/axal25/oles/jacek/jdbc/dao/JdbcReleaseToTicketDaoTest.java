package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.dao.EntityManagerTestUtils;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static axal25.oles.jacek.dao.EntityManagerTestUtils.OnSuccess.ROLLBACK;
import static axal25.oles.jacek.entity.factory.EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER;
import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcReleaseToTicketDaoTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void insertReleaseIdToTicketId_isInsertedSuccessfully() {
        Entry<ReleaseEntity, TicketEntity> inputReleaseToTicket =
                getInputReleaseToTicket("insertReleaseIdToTicketId_isInsertedSuccessfully");

        EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> insertReleaseIdToTicketIdAndAssert(inputReleaseToTicket, connection),
                ROLLBACK);
    }

    @Test
    public void selectReleaseIdToTicketIdMap() {
        Entry<ReleaseEntity, TicketEntity> inputReleaseToTicket =
                getInputReleaseToTicket("selectReleaseIdToTicketIdMap");

        Map<Integer, Integer> selectedReleaseIdToTicketIdMap = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    insertReleaseIdToTicketIdAndAssert(inputReleaseToTicket, connection);
                    return JdbcReleaseToTicketDao.selectReleaseIdToTicketIdMap(connection);
                },
                ROLLBACK);

        assertThat(selectedReleaseIdToTicketIdMap).containsAtLeast(
                inputReleaseToTicket.getKey().getId(),
                inputReleaseToTicket.getValue().getId());
    }

    private Entry<ReleaseEntity, TicketEntity> getInputReleaseToTicket(String methodName) {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                FROM_ENTITY_MANAGER);
        inputRelease.setApplications(List.of());

        TicketEntity inputTicket = TicketEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                null,
                FROM_ENTITY_MANAGER);
        inputTicket.setRelease(null);
        inputTicket.setApplication(null);

        return new SimpleEntry<>(inputRelease, inputTicket);
    }

    private Void insertReleaseIdToTicketIdAndAssert(
            Entry<ReleaseEntity, TicketEntity> inputReleaseToTicket,
            Connection connection) throws SQLException {

        JdbcReleaseDao.insertRelease(inputReleaseToTicket.getKey(), connection);
        JdbcTicketDao.insertTicket(inputReleaseToTicket.getValue(), connection);

        Optional<SimpleEntry<Integer, Integer>> insertedReleaseIdToTicketId =
                JdbcReleaseToTicketDao.insertReleaseIdToTicketId(
                        inputReleaseToTicket.getKey().getId(),
                        inputReleaseToTicket.getValue().getId(),
                        connection);

        assertThat(insertedReleaseIdToTicketId).isNotNull();
        assertThat(insertedReleaseIdToTicketId.isPresent()).isTrue();
        assertThat(insertedReleaseIdToTicketId.get().getKey()).isEqualTo(inputReleaseToTicket.getKey().getId());
        assertThat(insertedReleaseIdToTicketId.get().getValue()).isEqualTo(inputReleaseToTicket.getValue().getId());

        return null;
    }
}
