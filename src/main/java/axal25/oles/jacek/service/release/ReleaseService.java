package axal25.oles.jacek.service.release;

import axal25.oles.jacek.dao.release.IReleaseDao;
import axal25.oles.jacek.entity.ReleaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReleaseService implements IReleaseService {

    @Autowired
    private IReleaseDao releaseDao;

    @Override
    public List<ReleaseEntity> getAllReleases() {
        return releaseDao.getAllReleases();
    }

    @Override
    public List<ReleaseEntity> getAllReleasesEagerly() {
        return releaseDao.getAllReleasesEagerly();
    }

    @Override
    public void addRelease(ReleaseEntity release) {
        releaseDao.addRelease(release);
    }

    @Override
    public void addApplication(Integer applicationId, Integer releaseId) {
        releaseDao.addApplication(applicationId, releaseId);
    }
}
