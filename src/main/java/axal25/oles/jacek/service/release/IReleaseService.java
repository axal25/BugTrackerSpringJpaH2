package axal25.oles.jacek.service.release;

import axal25.oles.jacek.entity.ReleaseEntity;

public interface IReleaseService {
    void addRelease(ReleaseEntity release);

    void addApplication(Integer applicationId, Integer releaseId);
}
