package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.TicketEntity;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcTicketTransactionalDao {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(JdbcTicketTransactionalDao.class);
    private static final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(JdbcTicketTransactionalDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<TicketEntity> selectTicketsTransactional() {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork(this::selectTickets);
        }
    }

    private List<TicketEntity> selectTickets(Connection connection) throws SQLException {
        try {
            return JdbcTicketDao.selectTickets(connection);
        } catch (SQLException e) {
            connection.rollback();
            throw new JdbcDaoRuntimeException(
                    "Could not select " +
                            ApplicationEntity.class.getSimpleName() +
                            "s.",
                    e);
        }
    }


    public TicketEntity insertTicketTransactional(TicketEntity ticket) {
        try (Session session = entityManager.unwrap(Session.class)) {
            return session.doReturningWork((connection) ->
                    insertTicketTransactional(ticket, connection));
        }
    }

    private TicketEntity insertTicketTransactional(TicketEntity ticket, Connection connection) throws SQLException {
        Optional<TicketEntity> optTicket;

        try {
            optTicket = JdbcTicketDao.insertTicket(ticket, connection);
        } catch (SQLException e) {
            connection.rollback();
            throw new JdbcDaoRuntimeException(
                    "Could not insert " +
                            TicketEntity.class.getSimpleName() +
                            ": " +
                            ticket +
                            ".",
                    e);
        }

        if (optTicket.isEmpty()) {
            connection.rollback();
            throw new JdbcDaoRuntimeException("Returned " +
                    Optional.class +
                    "<" +
                    TicketEntity.class.getSimpleName() +
                    "> was " +
                    Optional.empty() +
                    ".");
        }

        connection.commit();

        slf4jLogger.debug("inserted {}: {}",
                TicketEntity.class.getSimpleName(),
                optTicket.get().toJsonPrettyString());
        log4jLogger.debug("inserted {}: {}",
                TicketEntity.class.getSimpleName(),
                optTicket.get().toJsonPrettyString());

        return optTicket.get();
    }
}
