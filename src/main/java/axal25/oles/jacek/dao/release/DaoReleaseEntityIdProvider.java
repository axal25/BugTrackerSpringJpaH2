package axal25.oles.jacek.dao.release;

import axal25.oles.jacek.context.application.ApplicationContextStaticInstanceProvider;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.jdbc.id.provider.AbstractEntityIdProvider;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoReleaseEntityIdProvider extends AbstractEntityIdProvider {

    private static DaoReleaseEntityIdProvider singleton = null;
    private static IReleaseDao releaseDao = null;

    private DaoReleaseEntityIdProvider() {
    }

    public static synchronized Integer generateId() {
        if (singleton == null) {
            singleton = new DaoReleaseEntityIdProvider();
        }

        return singleton.instanceGenerateId();
    }

    private static IReleaseDao getReleaseDao() {
        if (releaseDao == null) {
            releaseDao = ApplicationContextStaticInstanceProvider.getApplicationContext().getBean(IReleaseDao.class);
        }

        return releaseDao;
    }

    @Override
    protected Set<Integer> fetchIds() throws SQLException {
        return getReleaseDao()
                .getAllReleases().stream()
                .map(ReleaseEntity::getId)
                .collect(Collectors.toSet());
    }
}
