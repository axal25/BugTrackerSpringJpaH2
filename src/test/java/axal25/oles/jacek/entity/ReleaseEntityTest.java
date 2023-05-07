package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public class ReleaseEntityTest {

    public static ReleaseEntity getDeepCopy(ReleaseEntity inputRelease) {
        ReleaseEntity deepCopyRelease = new ReleaseEntity();

        deepCopyRelease.setId(inputRelease.getId());
        deepCopyRelease.setReleaseDate(inputRelease.getReleaseDate());
        deepCopyRelease.setDescription(inputRelease.getDescription());
        deepCopyRelease.setApplications(inputRelease.getApplications().isEmpty()
                ? List.of()
                : inputRelease.getApplications().stream()
                .map(ApplicationEntity::deepCopy)
                .collect(Collectors.toList()));

        return deepCopyRelease;
    }

    @Test
    public void deepCopy_isEqualToExpected() {
        ReleaseEntity release = ReleaseEntityFactory.produce(
                "deepCopy_isEqualToExpected",
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.RANDOM);

        assertThat(release.deepCopy()).isEqualTo(getDeepCopy(release));
    }
}
