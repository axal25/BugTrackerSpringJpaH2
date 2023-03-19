package axal25.oles.jacek.jdbc;

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

        return singleton.generatedId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return Set.copyOf(
                new JdbcApplicationDao()
                        .selectApplicationIds());
    }
}
