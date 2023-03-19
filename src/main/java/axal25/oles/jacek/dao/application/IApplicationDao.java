package axal25.oles.jacek.dao.application;

import axal25.oles.jacek.entity.ApplicationEntity;

import java.util.List;

public interface IApplicationDao {

    List<ApplicationEntity> getAllApplications();

    void addApplication(ApplicationEntity application);

    boolean applicationExists(String name, String owner);

    ApplicationEntity getApplicationById(int applicationId);

    void updateApplication(ApplicationEntity application);

    void deleteApplication(int applicationId);
}
