package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcApplicationDaoTest {
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
    public void selectApplications() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplications",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertApplicationAndAssert(inputApp);

        List<ApplicationEntity> selecteds = JdbcApplicationDao.selectApplications(connection);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp));
    }

    @Test
    public void selectApplicationIds() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationIds",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertApplicationAndAssert(inputApp);

        List<Integer> selecteds = JdbcApplicationDao.selectApplicationIds(connection);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp.getId()));
    }

    @Test
    public void selectApplicationById() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationById",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertApplicationAndAssert(inputApp);

        Optional<ApplicationEntity> selected = JdbcApplicationDao.selectApplicationById(inputApp.getId(), connection);

        assertThat(selected).isNotNull();
        assertThat(selected.isPresent()).isTrue();
        assertThat(selected.get()).isEqualTo(inputApp);
    }

    @Test
    public void selectApplicationsByIds() throws Exception {
        List<ApplicationEntity> inputApps = IntStream.range(0, random.nextInt(10))
                .mapToObj(unused -> ApplicationEntityFactory.produce(
                        "selectApplicationsByIds",
                        getClass(),
                        null,
                        EntityFactory.IdGenerateMode.FROM_JDBC))
                .collect(Collectors.toList());

        inputApps.forEach(inputApp -> {
            try {
                insertApplicationAndAssert(inputApp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        List<ApplicationEntity> selecteds = JdbcApplicationDao.selectApplicationsByIds(
                inputApps.stream().map(ApplicationEntity::getId).collect(Collectors.toList()),
                connection);

        assertThat(selecteds).containsExactlyElementsIn(inputApps);
    }

    @Test
    public void insertApplication_isInsertedSuccessfully() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "insertApplication_isInsertedSuccessfully",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        insertApplicationAndAssert(inputApp);
    }

    private void insertApplicationAndAssert(ApplicationEntity inputApp) throws SQLException {
        Optional<ApplicationEntity> inserted = JdbcApplicationDao.insertApplication(inputApp, connection);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputApp);
    }
}
