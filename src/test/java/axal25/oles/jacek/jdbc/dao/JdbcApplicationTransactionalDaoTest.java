package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcApplicationTransactionalDaoTest {

    @Autowired
    private JdbcApplicationTransactionalDao jdbcApplicationTransactionalDao;

    @Test
    public void insertApplicationTransactional_isInsertedSuccessfully() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "insertApplicationTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        ApplicationEntity inserted =
                jdbcApplicationTransactionalDao.insertApplicationTransactional(inputApp);

        assertThat(inserted).isEqualTo(inputApp);
    }
}
