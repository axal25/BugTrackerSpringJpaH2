package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.dao.EntityManagerTestUtils;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static axal25.oles.jacek.dao.EntityManagerTestUtils.OnSuccess.ROLLBACK;
import static axal25.oles.jacek.entity.factory.EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER;
import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcApplicationDaoTest {
    private static final Random random = new Random();
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void selectApplications() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplications",
                getClass(),
                null,
                FROM_ENTITY_MANAGER);

        List<ApplicationEntity> selecteds = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    insertApplicationAndAssert(inputApp, connection);
                    return JdbcApplicationDao.selectApplications(connection);
                },
                ROLLBACK);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp));
    }

    @Test
    public void selectApplicationIds() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationIds",
                getClass(),
                null,
                FROM_ENTITY_MANAGER);

        List<Integer> selecteds = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    insertApplicationAndAssert(inputApp, connection);
                    return JdbcApplicationDao.selectApplicationIds(connection);
                },
                ROLLBACK);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp.getId()));
    }

    @Test
    public void selectApplicationById() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationById",
                getClass(),
                null,
                FROM_ENTITY_MANAGER);

        Optional<ApplicationEntity> selected = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    insertApplicationAndAssert(inputApp, connection);
                    return JdbcApplicationDao.selectApplicationById(inputApp.getId(), connection);
                },
                ROLLBACK);

        assertThat(selected).isNotNull();
        assertThat(selected.isPresent()).isTrue();
        assertThat(selected.get()).isEqualTo(inputApp);
    }

    @Test
    public void selectApplicationsByIds() {
        List<ApplicationEntity> inputApps = IntStream.range(0, random.nextInt(10))
                .mapToObj(unused -> ApplicationEntityFactory.produce(
                        "selectApplicationsByIds",
                        getClass(),
                        null,
                        FROM_ENTITY_MANAGER))
                .collect(Collectors.toList());

        List<ApplicationEntity> selecteds = EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> {
                    inputApps.forEach(inputApp -> {
                        try {
                            insertApplicationAndAssert(inputApp, connection);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    return JdbcApplicationDao.selectApplicationsByIds(
                            inputApps.stream().map(ApplicationEntity::getId).collect(Collectors.toList()),
                            connection);
                },
                ROLLBACK);

        assertThat(selecteds).containsExactlyElementsIn(inputApps);
    }

    @Test
    public void insertApplication_isInsertedSuccessfully() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "insertApplication_isInsertedSuccessfully",
                getClass(),
                null,
                FROM_ENTITY_MANAGER);

        EntityManagerTestUtils.useHibernateConnection(
                entityManager,
                connection -> insertApplicationAndAssert(inputApp, connection),
                ROLLBACK);
    }

    private Void insertApplicationAndAssert(ApplicationEntity inputApp, Connection connection) throws SQLException {
        Optional<ApplicationEntity> inserted = JdbcApplicationDao.insertApplication(
                inputApp,
                connection);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputApp);
        return null;
    }
}
