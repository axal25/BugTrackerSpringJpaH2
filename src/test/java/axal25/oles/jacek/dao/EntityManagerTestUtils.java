package axal25.oles.jacek.dao;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;

public class EntityManagerTestUtils {
    public static <RETURN> RETURN useHibernateConnection(
            EntityManager entityManager,
            ThrowingFunction<Connection, RETURN> connectionConsumer,
            OnSuccess onSuccess) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork(connection -> {
                connection.setAutoCommit(false);
                try {
                    RETURN toBeReturned = connectionConsumer.applyOrThrow(connection);
                    onSuccess.apply(connection);
                    return toBeReturned;
                } catch (Exception e) {
                    connection.rollback();
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public enum OnSuccess implements IOnSuccess {
        ROLLBACK {
            @Override
            public void apply(Connection connection) throws SQLException {
                connection.rollback();
            }
        }, COMMIT {
            @Override
            public void apply(Connection connection) throws SQLException {
                connection.commit();
            }
        }
    }

    private interface IOnSuccess {
        default void apply(Connection connection) throws SQLException {
            throw new UnsupportedOperationException("onSuccess method not implemented for " + this);
        }
    }

    public interface ThrowingFunction<ARG, RETURN> {
        RETURN applyOrThrow(ARG arg) throws Exception;
    }
}
