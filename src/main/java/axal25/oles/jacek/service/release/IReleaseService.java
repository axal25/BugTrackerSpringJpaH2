package axal25.oles.jacek.service.release;

import axal25.oles.jacek.entity.ReleaseEntity;

import java.util.List;

public interface IReleaseService {

    List<ReleaseEntity> getAllReleases();

    List<ReleaseEntity> getAllReleasesEagerly();

    void addRelease(ReleaseEntity release);

    void addApplication(Integer applicationId, Integer releaseId);
}
