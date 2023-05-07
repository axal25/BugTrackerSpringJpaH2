package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.jdbc.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcReleaseTransactionalDao {
    private JdbcReleaseTransactionalDao() {
    }

    public static Optional<ReleaseEntity> insertReleaseTransactional(ReleaseEntity release) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
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
            return Optional.empty();
        }

        connection.commit();
        return optRelease;
    }
}
