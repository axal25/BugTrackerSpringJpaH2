package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.jdbc.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcTicketTransactionalDao {
    private JdbcTicketTransactionalDao() {
    }

    static Optional<TicketEntity> insertTicketTransactional(TicketEntity ticket) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
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

        connection.commit();
        return optTicket;
    }
}
