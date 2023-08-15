package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
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
public class JdbcApplicationTransactionalDao {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(JdbcApplicationTransactionalDao.class);
    private static final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(JdbcApplicationTransactionalDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public ApplicationEntity selectApplicationByIdTransactional(Integer id) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork((connection) -> selectApplicationById(id, connection));
        }
    }

    private ApplicationEntity selectApplicationById(Integer id, Connection connection) throws SQLException {
        Optional<ApplicationEntity> optApp;

        try {
            optApp = JdbcApplicationDao.selectApplicationById(id, connection);
        } catch (SQLException e) {
            connection.rollback();
            throw new JdbcDaoRuntimeException(
                    "Could not select " +
                            ApplicationEntity.class.getSimpleName() +
                            " with " +
                            Constants.Tables.Applications.ID +
                            ": " +
                            id +
                            ".",
                    e);
        }

        if (optApp.isEmpty()) {
            connection.rollback();
            throw new JdbcDaoRuntimeException("Returned " +
                    Optional.class +
                    "<" +
                    ApplicationEntity.class.getSimpleName() +
                    "> was " +
                    Optional.empty() +
                    ".");
        }

        return optApp.get();
    }

    public ApplicationEntity insertApplicationTransactional(ApplicationEntity inputApplication) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork((connection) -> insertApplication(inputApplication, connection));
        }
    }

    private ApplicationEntity insertApplication(ApplicationEntity application, Connection connection) throws SQLException {
        Optional<ApplicationEntity> optApp;

        try {
            optApp = JdbcApplicationDao.insertApplication(application, connection);
        } catch (SQLException e) {
            connection.rollback();
            throw new JdbcDaoRuntimeException(
                    "Could not insert " +
                            ApplicationEntity.class.getSimpleName() +
                            ": " +
                            application +
                            ".",
                    e);
        }

        if (optApp.isEmpty()) {
            connection.rollback();
            throw new JdbcDaoRuntimeException("Returned " +
                    Optional.class +
                    "<" +
                    ApplicationEntity.class.getSimpleName() +
                    "> was " +
                    Optional.empty() +
                    ".");
        }

        connection.commit();

        slf4jLogger.debug("inserted {}: {}",
                ApplicationEntity.class.getSimpleName(),
                optApp.get().toJsonPrettyString());
        log4jLogger.debug("inserted {}: {}",
                ApplicationEntity.class.getSimpleName(),
                optApp.get().toJsonPrettyString());

        return optApp.get();
    }
}
