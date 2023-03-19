package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.ReleaseEntity;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcReleaseEntityIdProvider extends JdbcAbstractEntityIdProvider {

    private static JdbcReleaseEntityIdProvider singleton = null;

    private JdbcReleaseEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new JdbcReleaseEntityIdProvider();
        }

        return singleton.generatedId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return new JdbcReleaseDao()
                .selectReleases().stream()
                .map(ReleaseEntity::getId)
                .collect(Collectors.toSet());
    }
}
