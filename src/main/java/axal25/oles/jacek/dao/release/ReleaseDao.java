package axal25.oles.jacek.dao.release;

import axal25.oles.jacek.dao.application.IApplicationDao;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class ReleaseDao implements IReleaseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IApplicationDao applicationDao;


    @Override
    public void addRelease(ReleaseEntity release) {
        entityManager.persist(release);
    }

    @Override
    public void addApplication(Integer applicationId, Integer releaseId) {
        ReleaseEntity release = getReleaseById(releaseId);
        ApplicationEntity application = applicationDao.getApplicationById(applicationId);
        release.getApplications().add(application);
        entityManager.flush();
    }

    @Override
    public ReleaseEntity getReleaseById(int releaseId) {
        return entityManager.find(ReleaseEntity.class, releaseId);
    }
}
