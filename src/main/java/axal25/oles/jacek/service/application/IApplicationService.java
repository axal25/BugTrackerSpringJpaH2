package axal25.oles.jacek.service.application;

import axal25.oles.jacek.entity.ApplicationEntity;

import java.util.List;

public interface IApplicationService {

    List<ApplicationEntity> getAllApplications();

    boolean addApplication(ApplicationEntity application);

    ApplicationEntity getApplicationById(int applicationId);

    void updateApplication(ApplicationEntity application);

    void deleteApplication(int applicationId);
}
