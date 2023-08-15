package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.context.application.ApplicationContextStaticInstanceProvider;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.jdbc.id.provider.AbstractEntityIdProvider;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoTicketEntityIdProvider extends AbstractEntityIdProvider {
    private static DaoTicketEntityIdProvider singleton = null;
    private static ITicketDao ticketDao = null;

    private DaoTicketEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new DaoTicketEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    private static ITicketDao getTicketDao() {
        if (ticketDao == null) {
            ticketDao = ApplicationContextStaticInstanceProvider.getApplicationContext().getBean("jtaDao", ITicketDao.class);
        }

        return ticketDao;
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return getTicketDao()
                .getAllTickets().stream()
                .map(TicketEntity::getId)
                .collect(Collectors.toSet());
    }
}
