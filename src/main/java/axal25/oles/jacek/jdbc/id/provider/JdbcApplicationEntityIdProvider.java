package axal25.oles.jacek.jdbc.id.provider;

import axal25.oles.jacek.jdbc.DatabaseUtils;
import axal25.oles.jacek.jdbc.dao.JdbcApplicationDao;

import java.sql.SQLException;
import java.util.Set;

public class JdbcApplicationEntityIdProvider extends JdbcAbstractEntityIdProvider {

    private static JdbcApplicationEntityIdProvider singleton = null;

    private JdbcApplicationEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new JdbcApplicationEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return Set.copyOf(
                JdbcApplicationDao
                        .selectApplicationIds(
                                DatabaseUtils.getConnection()));
    }
}
