package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcTicketTransactionalDaoTest {
    private static final Random random = new Random();

    @Autowired
    private JdbcTicketTransactionalDao jdbcTicketTransactionalDao;

    @Test
    public void selectTickets_areSelectedSuccessfully() {
        List<TicketEntity> inputTicket = IntStream.range(0, random.nextInt(10))
                .mapToObj(i -> TicketEntityFactory.produce(
                        "selectTickets_areSelectedSuccessfully",
                        getClass(),
                        null,
                        null,
                        null,
                        EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER))
                .collect(Collectors.toList());

        inputTicket.forEach(this::insertTicketTransactional);

        List<TicketEntity> selecteds = jdbcTicketTransactionalDao.selectTicketsTransactional();

        assertThat(selecteds).containsAtLeastElementsIn(inputTicket);
    }

    @Test
    public void insertTicketTransactional_isInsertedSuccessfully() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "insertTicketTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER);

        insertTicketTransactional(inputTicket);
    }

    private void insertTicketTransactional(TicketEntity inputTicket) {
        TicketEntity inserted = jdbcTicketTransactionalDao.insertTicketTransactional(inputTicket);

        assertThat(inserted).isEqualTo(inputTicket);
    }
}
