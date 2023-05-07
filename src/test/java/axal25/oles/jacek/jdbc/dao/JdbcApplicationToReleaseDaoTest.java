package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcApplicationToReleaseDaoTest {
    private static final Random random = new Random();
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
    public void selectApplicationIdToReleaseIdMap() throws SQLException {
        String methodName = "selectApplicationIdToReleaseIdMap";

        SimpleEntry<ApplicationEntity, ReleaseEntity> insertedAppToRelease =
                insertApplicationIdToReleaseIdAndAssertAndGet(methodName);

        Map<Integer, Integer> selectedApplicationIdToReleaseIdMap =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMap(connection);

        assertThat(selectedApplicationIdToReleaseIdMap)
                .containsAtLeast(insertedAppToRelease.getKey().getId(), insertedAppToRelease.getValue().getId());
    }

    @Test
    public void selectApplicationIdToReleaseIdByApplicationId() throws SQLException {
        String methodName = "selectApplicationIdToReleaseIdByApplicationId";

        SimpleEntry<ApplicationEntity, ReleaseEntity> insertedAppToRelease =
                insertApplicationIdToReleaseIdAndAssertAndGet(methodName);

        Optional<SimpleEntry<Integer, Integer>> applicationIdToReleaseId =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdByApplicationId(
                        insertedAppToRelease.getKey().getId(),
                        connection);

        assertThat(applicationIdToReleaseId).isNotNull();
        assertThat(applicationIdToReleaseId.isPresent()).isTrue();
        assertThat(applicationIdToReleaseId.get())
                .isEqualTo(new SimpleEntry<>(
                        insertedAppToRelease.getKey().getId(),
                        insertedAppToRelease.getValue().getId()));
    }

    @Test
    public void selectApplicationIdToReleaseIdByReleaseId() throws SQLException {
        String methodName = "selectApplicationIdToReleaseIdByReleaseId";

        SimpleEntry<ApplicationEntity, ReleaseEntity> insertedAppToRelease =
                insertApplicationIdToReleaseIdAndAssertAndGet(methodName);

        Optional<SimpleEntry<Integer, Integer>> applicationIdToReleaseId =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdByReleaseId(
                        insertedAppToRelease.getValue().getId(),
                        connection);

        assertThat(applicationIdToReleaseId).isNotNull();
        assertThat(applicationIdToReleaseId.isPresent()).isTrue();
        assertThat(applicationIdToReleaseId.get())
                .isEqualTo(new SimpleEntry<>(
                        insertedAppToRelease.getKey().getId(),
                        insertedAppToRelease.getValue().getId()));
    }

    @Test
    public void selectApplicationIdToReleaseIdMapByApplicationIds() throws SQLException {
        String methodName = "selectApplicationIdToReleaseIdMapByApplicationIds";

        Map<ApplicationEntity, ReleaseEntity> insertedAppToReleaseMap =
                IntStream.range(0, random.nextInt(10))
                        .mapToObj(unused -> {
                            try {
                                return insertApplicationIdToReleaseIdAndAssertAndGet(methodName);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toMap(
                                SimpleEntry::getKey,
                                SimpleEntry::getValue));

        Map<Integer, Integer> applicationIdToReleaseIdMap =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMapByApplicationIds(
                        insertedAppToReleaseMap.keySet().stream()
                                .map(ApplicationEntity::getId)
                                .collect(Collectors.toList()),
                        connection);

        assertThat(applicationIdToReleaseIdMap)
                .containsExactlyEntriesIn(
                        insertedAppToReleaseMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        applicationIdToReleaseId -> applicationIdToReleaseId.getKey().getId(),
                                        applicationIdToReleaseId -> applicationIdToReleaseId.getValue().getId())));
    }

    @Test
    public void selectApplicationIdToReleaseIdMapByReleaseIds() throws SQLException {
        String methodName = "selectApplicationIdToReleaseIdMapByApplicationIds";

        Map<ApplicationEntity, ReleaseEntity> insertedAppToReleaseMap =
                IntStream.range(0, random.nextInt(10))
                        .mapToObj(unused -> {
                            try {
                                return insertApplicationIdToReleaseIdAndAssertAndGet(methodName);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toMap(
                                SimpleEntry::getKey,
                                SimpleEntry::getValue));

        Map<Integer, Integer> applicationIdToReleaseIdMap =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMapByReleaseIds(
                        insertedAppToReleaseMap.values().stream()
                                .map(ReleaseEntity::getId)
                                .collect(Collectors.toList()),
                        connection);

        assertThat(applicationIdToReleaseIdMap)
                .containsExactlyEntriesIn(
                        insertedAppToReleaseMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        applicationIdToReleaseId -> applicationIdToReleaseId.getKey().getId(),
                                        applicationIdToReleaseId -> applicationIdToReleaseId.getValue().getId())));
    }

    @Test
    public void insertApplicationIdToReleaseId_isInsertedSuccessfully() throws SQLException {
        insertApplicationIdToReleaseIdAndAssertAndGet(
                "insertApplicationIdToReleaseId_isInsertedSuccessfully");
    }

    private SimpleEntry<ApplicationEntity, ReleaseEntity> insertApplicationIdToReleaseIdAndAssertAndGet(
            String methodName) throws SQLException {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                methodName,
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);
        inputRelease.setApplications(List.of());

        JdbcApplicationDao.insertApplication(inputApp, connection);
        JdbcReleaseDao.insertRelease(inputRelease, connection);

        Optional<SimpleEntry<Integer, Integer>> insertedAppIdToReleaseId =
                JdbcApplicationToReleaseDao.insertApplicationIdToReleaseId(
                        inputApp.getId(),
                        inputRelease.getId(),
                        connection);

        assertThat(insertedAppIdToReleaseId).isNotNull();
        assertThat(insertedAppIdToReleaseId.isPresent()).isTrue();
        assertThat(insertedAppIdToReleaseId.get().getKey()).isEqualTo(inputApp.getId());
        assertThat(insertedAppIdToReleaseId.get().getValue()).isEqualTo(inputRelease.getId());

        return new SimpleEntry<>(inputApp, inputRelease);
    }
}
