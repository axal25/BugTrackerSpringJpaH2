package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcApplicationTransactionalDaoTest {
    @Test
    public void insertApplicationTransactional_isInsertedSuccessfully() throws Exception {
        // TODO: restore
        if (true)
            return;

        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "insertApplicationTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        Optional<ApplicationEntity> inserted = JdbcApplicationTransactionalDao.insertApplicationTransactional(inputApp);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputApp);
    }
}
