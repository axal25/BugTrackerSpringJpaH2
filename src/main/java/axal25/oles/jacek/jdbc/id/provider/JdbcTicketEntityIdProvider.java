package axal25.oles.jacek.jdbc.id.provider;

import axal25.oles.jacek.jdbc.DatabaseUtils;
import axal25.oles.jacek.jdbc.dao.JdbcTicketDao;

import java.sql.SQLException;
import java.util.Set;

public class JdbcTicketEntityIdProvider extends AbstractEntityIdProvider {

    private static JdbcTicketEntityIdProvider singleton = null;

    private JdbcTicketEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new JdbcTicketEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return Set.copyOf(
                JdbcTicketDao
                        .selectTicketIds(
                                DatabaseUtils.getConnection()));
    }
}
