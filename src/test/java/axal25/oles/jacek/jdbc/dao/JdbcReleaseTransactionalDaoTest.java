package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcReleaseTransactionalDaoTest {

    @Autowired
    private JdbcReleaseTransactionalDao jdbcReleaseTransactionalDao;

    @Test
    public void insertReleaseTransactional_isInsertedSuccessfully() {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "insertReleaseTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        ReleaseEntity inserted =
                jdbcReleaseTransactionalDao.insertReleaseTransactional(inputRelease);

        assertThat(inserted).isEqualTo(inputRelease);
    }
}
