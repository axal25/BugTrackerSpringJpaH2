package axal25.oles.jacek.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.function.Consumer;

@Repository
@Transactional
public class DaoTestHelper {

    @PersistenceContext
    private EntityManager entityManager;

    public void wrapInTransaction(Consumer<EntityManager> entityManagerConsumer) {
        entityManagerConsumer.accept(entityManager);
    }
}
