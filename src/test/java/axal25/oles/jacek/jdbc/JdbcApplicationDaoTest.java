package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    private JdbcApplicationDao appDao;

    @BeforeEach
    void setUp() throws SQLException {
        appDao = new JdbcApplicationDao();
    }

    @Test
    public void selectApplications() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplications",
                getClass(),
                JdbcApplicationEntityIdProvider.generateId());

        insertApplicationAndAssert(inputApp);

        List<ApplicationEntity> selecteds = appDao.selectApplications();

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp));
    }

    @Test
    public void selectApplicationIds() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationIds",
                getClass(),
                JdbcApplicationEntityIdProvider.generateId());

        insertApplicationAndAssert(inputApp);

        List<Integer> selecteds = appDao.selectApplicationIds();

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputApp.getId()));
    }

    @Test
    public void selectApplicationById() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "selectApplicationById",
                getClass(),
                JdbcApplicationEntityIdProvider.generateId());

        insertApplicationAndAssert(inputApp);

        Optional<ApplicationEntity> selected = appDao.selectApplicationById(inputApp.getId());

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
                        JdbcApplicationEntityIdProvider.generateId()))
                .collect(Collectors.toList());

        inputApps.forEach(inputApp -> {
            try {
                insertApplicationAndAssert(inputApp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        List<ApplicationEntity> selecteds = appDao.selectApplicationsByIds(
                inputApps.stream().map(ApplicationEntity::getId).collect(Collectors.toList()));

        assertThat(selecteds).containsExactlyElementsIn(inputApps);
    }

    @Test
    public void insertApplication_isInsertedSuccessfully() throws Exception {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "insertApplication_isInsertedSuccessfully",
                getClass(),
                JdbcApplicationEntityIdProvider.generateId());

        insertApplicationAndAssert(inputApp);
    }

    public void insertApplicationAndAssert(ApplicationEntity inputApp) throws SQLException {
        Optional<ApplicationEntity> inserted = appDao.insertApplication(inputApp);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputApp);
    }
}
