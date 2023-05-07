package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.jdbc.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcApplicationTransactionalDao {
    private JdbcApplicationTransactionalDao() {
    }

    public static Optional<ApplicationEntity> insertApplicationTransactional(ApplicationEntity application) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
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
            return Optional.empty();
        }

        connection.commit();
        return optApp;
    }
}
