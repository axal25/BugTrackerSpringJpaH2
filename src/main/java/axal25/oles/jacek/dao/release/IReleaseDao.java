package axal25.oles.jacek.dao.release;

import axal25.oles.jacek.entity.ReleaseEntity;

import java.util.List;

public interface IReleaseDao {
    void addRelease(ReleaseEntity release);

    void addApplication(Integer applicationId, Integer releaseId);

    ReleaseEntity getReleaseById(int releaseId);

    List<ReleaseEntity> getAllReleases();
}
