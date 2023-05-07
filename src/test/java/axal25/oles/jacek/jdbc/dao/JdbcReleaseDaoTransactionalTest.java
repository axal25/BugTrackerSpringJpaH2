package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcReleaseDaoTransactionalTest {

    @Test
    public void insertReleaseTransactional_isInsertedSuccessfully() throws SQLException {
        // TODO: restore
        if (true)
            return;
        
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "insertReleaseTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        Optional<ReleaseEntity> inserted = JdbcReleaseTransactionalDao.insertReleaseTransactional(inputRelease);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputRelease);
    }
}
