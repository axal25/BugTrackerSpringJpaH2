package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.dao.EntityManagerTestUtils;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
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
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static axal25.oles.jacek.dao.EntityManagerTestUtils.OnSuccess.ROLLBACK;
import static axal25.oles.jacek.entity.factory.EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER;
import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.toMap;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcApplicationToReleaseDaoTest {
    private static final Random random = new Random();
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void selectApplicationIdToReleaseIdMap() {
        Map<ApplicationEntity, ReleaseEntity> inputAppsToReleases =
                getInputAppsToReleases("selectApplicationIdToReleaseIdMap");

        Map<Integer, Integer> actualSelectedApplicationIdToReleaseIdMap =
                EntityManagerTestUtils.useHibernateConnection(
                        entityManager,
                        connection -> {
                            insertApplicationIdsToReleaseIdsAndAssertAndGet(inputAppsToReleases, connection);
                            return JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMap(connection);
                        },
                        ROLLBACK);

        Map<Integer, Integer> expectedSelectedApplicationIdToReleaseIdMap =
                inputAppsToReleases.entrySet().stream()
                        .collect(toMap(
                                inputAppToRelease -> inputAppToRelease.getKey().getId(),
                                inputAppToRelease -> inputAppToRelease.getValue().getId()));
        assertThat(actualSelectedApplicationIdToReleaseIdMap)
                .containsAtLeastEntriesIn(expectedSelectedApplicationIdToReleaseIdMap);
    }

    @Test
    public void selectApplicationIdToReleaseIdByApplicationId() {
        Entry<ApplicationEntity, ReleaseEntity> inputAppToRelease =
                getInputAppToRelease(
                        "selectApplicationIdToReleaseIdByApplicationId",
                        true);

        Optional<SimpleEntry<Integer, Integer>> applicationIdToReleaseId =
                EntityManagerTestUtils.useHibernateConnection(
                        entityManager,
                        connection -> {
                            insertApplicationIdToReleaseIdAndAssertAndGet(inputAppToRelease, connection);
                            return JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdByApplicationId(
                                    inputAppToRelease.getKey().getId(),
                                    connection);
                        },
                        ROLLBACK);

        assertThat(applicationIdToReleaseId).isNotNull();
        assertThat(applicationIdToReleaseId.isPresent()).isTrue();
        assertThat(applicationIdToReleaseId.get())
                .isEqualTo(new SimpleEntry<>(
                        inputAppToRelease.getKey().getId(),
                        inputAppToRelease.getValue().getId()));
    }

    @Test
    public void selectApplicationIdToReleaseIdByReleaseId() {
        Entry<ApplicationEntity, ReleaseEntity> inputAppToRelease =
                getInputAppToRelease(
                        "selectApplicationIdToReleaseIdByReleaseId",
                        true);


        Map<Integer, Integer> actualApplicationIdsToReleaseIds =
                EntityManagerTestUtils.useHibernateConnection(
                        entityManager,
                        connection -> {
                            insertApplicationIdToReleaseIdAndAssertAndGet(inputAppToRelease, connection);
                            return JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdByReleaseId(
                                    inputAppToRelease.getValue().getId(),
                                    connection);
                        },
                        ROLLBACK);

        assertThat(actualApplicationIdsToReleaseIds).isNotNull();
        assertThat(actualApplicationIdsToReleaseIds.isEmpty()).isFalse();
        Map<ApplicationEntity, ReleaseEntity> inputNestedApplicationToRelease = inputAppToRelease.getValue().getApplications().stream()
                .map(app -> new SimpleEntry<>(app, inputAppToRelease.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue));
        Map<Integer, Integer> expectedApplicationIdsToReleaseIds = Stream.of(
                        inputNestedApplicationToRelease.entrySet().stream(),
                        Stream.of(inputAppToRelease))
                .flatMap(i -> i)
                .collect(toMap(
                        inputAppToRelease2 -> inputAppToRelease2.getKey().getId(),
                        inputAppToRelease2 -> inputAppToRelease2.getValue().getId()));
        assertThat(actualApplicationIdsToReleaseIds)
                .containsAtLeastEntriesIn(expectedApplicationIdsToReleaseIds);
    }

    @Test
    public void selectApplicationIdToReleaseIdMapByApplicationIds() {
        Map<ApplicationEntity, ReleaseEntity> inputAppsToReleases =
                getInputAppsToReleases("selectApplicationIdToReleaseIdMapByApplicationIds");

        Map<Integer, Integer> actualApplicationIdToReleaseIdMap =
                EntityManagerTestUtils.useHibernateConnection(
                        entityManager,
                        connection -> {
                            insertApplicationIdsToReleaseIdsAndAssertAndGet(inputAppsToReleases, connection);
                            return JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMapByApplicationIds(
                                    inputAppsToReleases.keySet().stream()
                                            .map(ApplicationEntity::getId)
                                            .collect(Collectors.toList()),
                                    connection);
                        },
                        ROLLBACK);

        Map<Integer, Integer> expectedApplicationIdToReleaseIdMap = inputAppsToReleases.entrySet().stream()
                .collect(toMap(
                        applicationIdToReleaseId -> applicationIdToReleaseId.getKey().getId(),
                        applicationIdToReleaseId -> applicationIdToReleaseId.getValue().getId()));
        assertThat(actualApplicationIdToReleaseIdMap).containsAtLeastEntriesIn(expectedApplicationIdToReleaseIdMap);
    }

    @Test
    public void selectApplicationIdToReleaseIdMapByReleaseIds() {
        Map<ApplicationEntity, ReleaseEntity> inputAppsToReleases =
                getInputAppsToReleases("selectApplicationIdToReleaseIdMapByApplicationIds");

        Map<Integer, Integer> actualApplicationIdToReleaseIdMap = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    insertApplicationIdsToReleaseIdsAndAssertAndGet(inputAppsToReleases, connection);
                    return JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMapByReleaseIds(
                            inputAppsToReleases.values().stream()
                                    .map(ReleaseEntity::getId)
                                    .collect(Collectors.toList()),
                            connection);
                },
                ROLLBACK);

        Map<Integer, Integer> expectedApplicationIdToReleaseIdMap = inputAppsToReleases.entrySet().stream()
                .collect(toMap(
                        applicationIdToReleaseId -> applicationIdToReleaseId.getKey().getId(),
                        applicationIdToReleaseId -> applicationIdToReleaseId.getValue().getId()));
        assertThat(actualApplicationIdToReleaseIdMap).containsExactlyEntriesIn(expectedApplicationIdToReleaseIdMap);
    }

    @Test
    public void insertApplicationIdToReleaseId_isInsertedSuccessfully() {
        Entry<ApplicationEntity, ReleaseEntity> inputAppToRelease =
                getInputAppToRelease(
                        "insertApplicationIdToReleaseId_isInsertedSuccessfully",
                        true);

        EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> insertApplicationIdToReleaseIdAndAssertAndGet(
                        inputAppToRelease,
                        connection),
                ROLLBACK);
    }

    private Map<ApplicationEntity, ReleaseEntity> getInputAppsToReleases(String methodName) {
        return IntStream.range(0, random.nextInt(10))
                .mapToObj(unused -> getInputAppToRelease(methodName, false))
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    private Entry<ApplicationEntity, ReleaseEntity> getInputAppToRelease(String methodName, boolean hasNestedApps) {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                methodName,
                getClass(),
                null,
                FROM_ENTITY_MANAGER);
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                methodName,
                getClass(),
                null,
                null,
                FROM_ENTITY_MANAGER);
        if (!hasNestedApps) {
            inputRelease.setApplications(List.of());
        }
        return new SimpleEntry<>(inputApp, inputRelease);
    }

    private void insertApplicationIdsToReleaseIdsAndAssertAndGet(
            Map<ApplicationEntity, ReleaseEntity> inputAppsToReleases, Connection connection) {
        inputAppsToReleases.entrySet().forEach(inputAppToRelease ->
                insertApplicationIdToReleaseIdAndAssertAndGet(inputAppToRelease, connection));
    }

    private Void insertApplicationIdToReleaseIdAndAssertAndGet(
            Entry<ApplicationEntity, ReleaseEntity> inputAppToRelease, Connection connection) {
        Optional<SimpleEntry<Integer, Integer>> insertedAppIdToReleaseId;

        try {
            JdbcApplicationDao.insertApplication(inputAppToRelease.getKey(), connection);
            JdbcReleaseDao.insertRelease(inputAppToRelease.getValue(), connection);
            insertedAppIdToReleaseId = JdbcApplicationToReleaseDao.insertApplicationIdToReleaseId(
                    inputAppToRelease.getKey().getId(),
                    inputAppToRelease.getValue().getId(),
                    connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        assertThat(insertedAppIdToReleaseId).isNotNull();
        assertThat(insertedAppIdToReleaseId.isPresent()).isTrue();
        assertThat(insertedAppIdToReleaseId.get().getKey()).isEqualTo(inputAppToRelease.getKey().getId());
        assertThat(insertedAppIdToReleaseId.get().getValue()).isEqualTo(inputAppToRelease.getValue().getId());

        return null;
    }
}
