package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class JdbcReleaseTransactionalDao {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(JdbcReleaseTransactionalDao.class);
    private static final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(JdbcReleaseTransactionalDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public ReleaseEntity insertReleaseTransactional(ReleaseEntity release) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork((connection) ->
                    insertReleaseTransactional(release, connection));
        }
    }

    private ReleaseEntity insertReleaseTransactional(ReleaseEntity release, Connection connection) throws SQLException {
        Optional<ReleaseEntity> optRelease;

        try {
            optRelease = JdbcReleaseDao.insertRelease(release, connection);
        } catch (SQLException e) {
            connection.rollback();
            throw new JdbcDaoRuntimeException(
                    "Could not insert " +
                            ReleaseEntity.class.getSimpleName() +
                            ": " +
                            release +
                            ".",
                    e);
        }

        if (optRelease.isEmpty()) {
            connection.rollback();
            throw new JdbcDaoRuntimeException("Returned " +
                    Optional.class +
                    "<" +
                    ReleaseEntity.class.getSimpleName() +
                    "> was " +
                    Optional.empty() +
                    ".");
        }

        connection.commit();

        slf4jLogger.error("inserted {}: {}",
                ReleaseEntity.class.getSimpleName(),
                release.toJsonPrettyString());
        log4jLogger.error("inserted {}: {}",
                ReleaseEntity.class.getSimpleName(),
                release.toJsonPrettyString());

        return optRelease.get();
    }
}
