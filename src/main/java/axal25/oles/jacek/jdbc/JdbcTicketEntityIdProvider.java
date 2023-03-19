package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.entity.TicketEntity;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcTicketEntityIdProvider extends JdbcAbstractEntityIdProvider {

    private static JdbcTicketEntityIdProvider singleton = null;

    private JdbcTicketEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new JdbcTicketEntityIdProvider();
        }

        return singleton.generatedId();
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return new JdbcTicketDao()
                .selectTickets().stream()
                .map(TicketEntity::getId)
                .collect(Collectors.toSet());
    }
}
