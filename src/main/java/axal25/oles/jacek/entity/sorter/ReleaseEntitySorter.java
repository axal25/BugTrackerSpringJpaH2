package axal25.oles.jacek.entity.sorter;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReleaseEntitySorter {
    public static Stream<ReleaseEntity> sorted(
            Stream<ReleaseEntity> releases,
            Comparator<? super ReleaseEntity> releaseComparator,
            Comparator<? super ApplicationEntity> applicationComparator) {
        return releases == null ? null : releases.map(release -> sorted(release, applicationComparator))
                .sorted(releaseComparator);
    }

    public static ReleaseEntity sorted(ReleaseEntity release, Comparator<? super ApplicationEntity> applicationComparator) {
        return release == null ? null
                : release.getApplications() == null ? release
                : release.toBuilder()
                .applications(
                        ApplicationEntitySorter.sorted(
                                        applicationComparator,
                                        release.getApplications().stream())
                                .collect(Collectors.toList()))
                .build();
    }
}
