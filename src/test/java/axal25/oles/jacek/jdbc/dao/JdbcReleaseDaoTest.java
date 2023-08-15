package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static axal25.oles.jacek.entity.factory.EntityFactory.IdGenerateMode.FROM_JDBC;
import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcReleaseDaoTest {
    // TODO: rework
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
    public void selectReleaseIds() throws Exception {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "selectReleaseIds",
                getClass(),
                null,
                null,
                FROM_JDBC);

        insertReleaseAndAssert(inputRelease);

        List<Integer> selecteds = JdbcReleaseDao.selectReleaseIds(connection);

        assertThat(selecteds).containsAtLeastElementsIn(List.of(inputRelease.getId()));
    }

    @Test
    public void selectReleases() throws SQLException {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "selectReleases",
                getClass(),
                null,
                null,
                FROM_JDBC);

        insertReleaseAndAssert(inputRelease);

        List<ReleaseEntity> selected = JdbcReleaseDao.selectReleases(connection);

        assertThat(selected).containsAtLeastElementsIn(List.of(inputRelease));
    }

    @Test
    public void insertRelease_isInsertedSuccessfully() throws SQLException {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "insertRelease_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                FROM_JDBC);

        insertReleaseAndAssert(inputRelease);
    }

    private void insertReleaseAndAssert(ReleaseEntity inputRelease) throws SQLException {
        Optional<ReleaseEntity> inserted = JdbcReleaseDao.insertRelease(inputRelease, connection);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputRelease);
    }
}
