package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcReleaseDaoTest {

    private JdbcReleaseDao releaseDao;

    @BeforeEach
    void setUp() throws SQLException {
        releaseDao = new JdbcReleaseDao();
    }

    @Test
    public void selectReleases() throws SQLException {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "selectReleases",
                getClass(),
                null,
                null);

        insertReleaseAndAssert(inputRelease);

        List<ReleaseEntity> selected = releaseDao.selectReleases();

        assertThat(selected).containsAtLeastElementsIn(List.of(inputRelease));
    }

    @Test
    public void insertRelease_isInsertedSuccessfully() throws SQLException {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "insertRelease_isInsertedSuccessfully",
                getClass(),
                null,
                null);

        insertReleaseAndAssert(inputRelease);
    }

    public void insertReleaseAndAssert(ReleaseEntity inputRelease) throws SQLException {
        Optional<ReleaseEntity> inserted = releaseDao.insertRelease(inputRelease);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputRelease);
    }
}
