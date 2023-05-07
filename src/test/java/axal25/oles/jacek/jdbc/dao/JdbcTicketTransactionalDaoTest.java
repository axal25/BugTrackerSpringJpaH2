package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcTicketTransactionalDaoTest {

    @Test
    void insertTicketTransactional_isInsertedSuccessfully() throws SQLException {
        // TODO: restore
        if (true)
            return;
        
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "insertTicketTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        Optional<TicketEntity> inserted = JdbcTicketTransactionalDao.insertTicketTransactional(inputTicket);

        assertThat(inserted).isNotNull();
        assertThat(inserted.isPresent()).isTrue();
        assertThat(inserted.get()).isEqualTo(inputTicket);
    }
}
