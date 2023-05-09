package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcTicketTransactionalDaoTest {

    @Autowired
    private JdbcTicketTransactionalDao jdbcTicketTransactionalDao;

    @Test
    public void insertTicketTransactional_isInsertedSuccessfully() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "insertTicketTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_JDBC);

        TicketEntity inserted = jdbcTicketTransactionalDao.insertTicketTransactional(inputTicket);

        assertThat(inserted).isEqualTo(inputTicket);
    }
}
