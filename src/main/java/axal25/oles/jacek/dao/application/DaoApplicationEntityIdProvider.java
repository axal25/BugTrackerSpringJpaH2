package axal25.oles.jacek.dao.application;

import axal25.oles.jacek.context.application.ApplicationContextStaticInstanceProvider;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.jdbc.id.provider.AbstractEntityIdProvider;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoApplicationEntityIdProvider extends AbstractEntityIdProvider {

    private static DaoApplicationEntityIdProvider singleton = null;
    private static IApplicationDao applicationDao = null;

    private DaoApplicationEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new DaoApplicationEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    private static IApplicationDao getApplicationDao() {
        if (applicationDao == null) {
            applicationDao = ApplicationContextStaticInstanceProvider.getApplicationContext().getBean(IApplicationDao.class);
        }

        return applicationDao;
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return getApplicationDao()
                .getAllApplications().stream()
                .map(ApplicationEntity::getId)
                .collect(Collectors.toSet());
    }
}
