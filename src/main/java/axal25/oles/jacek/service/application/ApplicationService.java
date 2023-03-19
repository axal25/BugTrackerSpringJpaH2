package axal25.oles.jacek.service.application;

import axal25.oles.jacek.dao.application.IApplicationDao;
import axal25.oles.jacek.entity.ApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationService implements IApplicationService {

    @Autowired
    private IApplicationDao applicationDao;

    @Override
    public List<ApplicationEntity> getAllApplications() {
        return applicationDao.getAllApplications();
    }

    @Override
    public boolean addApplication(ApplicationEntity application) {
        if (applicationDao.applicationExists(application.getName(), application.getOwner())) {
            return false;
        }
        applicationDao.addApplication(application);
        return true;
    }

    @Override
    public ApplicationEntity getApplicationById(int applicationId) {
        return applicationDao.getApplicationById(applicationId);
    }

    @Override
    public void updateApplication(ApplicationEntity application) {
        applicationDao.updateApplication(application);
    }

    @Override
    public void deleteApplication(int applicationId) {
        applicationDao.deleteApplication(applicationId);
    }
}
