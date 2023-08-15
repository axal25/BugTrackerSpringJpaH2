package axal25.oles.jacek.jdbc.id.provider;

import axal25.oles.jacek.jdbc.DatabaseUtils;
import axal25.oles.jacek.jdbc.dao.JdbcReleaseDao;

import java.sql.SQLException;
import java.util.Set;

public class JdbcReleaseEntityIdProvider extends AbstractEntityIdProvider {

    private static JdbcReleaseEntityIdProvider singleton = null;

    private JdbcReleaseEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new JdbcReleaseEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return Set.copyOf(
                JdbcReleaseDao
                        .selectReleaseIds(
                                DatabaseUtils.getConnection()));
    }
}
