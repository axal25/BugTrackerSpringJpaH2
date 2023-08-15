package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import axal25.oles.jacek.entity.sorter.ReleaseEntitySorter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

public class ReleaseEntityTest {
    private static final List<ReleaseEntity> releasesExpectedOrder = Stream.of(
            null,
            new ReleaseEntity(),

            ReleaseEntity.builder()
                    .applications(List.of())
                    .build(),
            ReleaseEntity.builder()
                    .applications(List.of(new ApplicationEntity()))
                    .build(),
            ReleaseEntity.builder()
                    .applications(List.of(new ApplicationEntity(), new ApplicationEntity()))
                    .build(),
            ReleaseEntity.builder()
                    .applications(List.of(
                            new ApplicationEntity(),
                            ApplicationEntity.builder().id(1).build(),
                            ApplicationEntity.builder().id(2).build(),
                            ApplicationEntity.builder().id(3).build(),
                            ApplicationEntity.builder().id(4).build(),
                            ApplicationEntity.builder().id(5).build()))
                    .build(),

            ReleaseEntity.builder()
                    .releaseDate(LocalDate.of(2000, 1, 1))
                    .build(),
            ReleaseEntity.builder()
                    .releaseDate(LocalDate.of(2000, 1, 2))
                    .build(),
            ReleaseEntity.builder()
                    .releaseDate(LocalDate.of(2000, 1, 2))
                    .applications(List.of())
                    .build(),

            ReleaseEntity.builder()
                    .description("")
                    .build(),
            ReleaseEntity.builder()
                    .description("1")
                    .build(),
            ReleaseEntity.builder()
                    .description("2")
                    .build(),
            ReleaseEntity.builder()
                    .description("2")
                    .releaseDate(LocalDate.of(2000, 1, 3))
                    .build(),
            ReleaseEntity.builder()
                    .description("2")
                    .releaseDate(LocalDate.of(2000, 1, 3))
                    .applications(List.of())
                    .build(),

            ReleaseEntity.builder()
                    .id(1)
                    .build(),
            ReleaseEntity.builder()
                    .id(2)
                    .build(),
            ReleaseEntity.builder()
                    .id(2)
                    .description("")
                    .build(),
            ReleaseEntity.builder()
                    .id(2)
                    .description("1")
                    .build(),
            ReleaseEntity.builder()
                    .id(2)
                    .description("1")
                    .releaseDate(LocalDate.of(2000, 1, 3))
                    .build(),
            ReleaseEntity.builder()
                    .id(2)
                    .description("1")
                    .releaseDate(LocalDate.of(2000, 1, 3))
                    .applications(List.of())
                    .build()
    ).collect(Collectors.toList());

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

    public static List<ReleaseEntity> getRandomizedOrderNested(List<ReleaseEntity> releases) {
        if (releases == null) {
            return null;
        }
        return getRandomizedOrderTopLayerOnly(releases).stream()
                .map(ReleaseEntityTest::getRandomizedOrderNested)
                .collect(Collectors.toList());
    }

    private static List<ReleaseEntity> getRandomizedOrderTopLayerOnly(List<ReleaseEntity> releases) {
        List<ReleaseEntity> shuffled = new ArrayList<>(releases);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static ReleaseEntity getRandomizedOrderNested(ReleaseEntity release) {
        return release == null ? null : release.toBuilder()
                .applications(ApplicationEntityTest.getRandomizedOrder(release.getApplications()))
                .build();
    }

    @Test
    public void deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal() {
        ReleaseEntity input = ReleaseEntityFactory.produce(
                "deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal",
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.RANDOM);
        ReleaseEntity expected = getDeepCopy(input);

        ReleaseEntity actual = input.deepCopy();

        assertThat(actual).isEqualTo(input);
        assertThat(actual).isEqualTo(expected);

        actual = input.deepCopy();
        actual.setId(actual.getId() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setReleaseDate(actual.getReleaseDate().plusDays(1));
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setDescription(actual.getDescription() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setApplications(null);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setApplications(List.of(actual.getApplications().get(0), actual.getApplications().get(0)));
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.getApplications().get(0).setId(actual.getApplications().get(0).getId() + 1);
        assertThat(actual).isNotEqualTo(input);
    }

    @Test
    void compareTo_isSortingAsExpected_topLayerOnly() {
        List<ReleaseEntity> releasesRandomOrder;
        do {
            releasesRandomOrder = getRandomizedOrderTopLayerOnly(releasesExpectedOrder);
        } while (Objects.equals(releasesRandomOrder, releasesExpectedOrder));

        List<ReleaseEntity> releasesNullFirstNaturalOrder = releasesRandomOrder.stream()
                .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                .collect(Collectors.toList());
        List<ReleaseEntity> releasesReleaseComparatorOrder = releasesRandomOrder.stream()
                .sorted(ReleaseEntity.RELEASE_COMPARATOR)
                .collect(Collectors.toList());

        assertThat(releasesNullFirstNaturalOrder).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertThat(releasesReleaseComparatorOrder).isInOrder(ReleaseEntity.RELEASE_COMPARATOR);

        assertThat(releasesNullFirstNaturalOrder).isEqualTo(releasesExpectedOrder);
        assertThat(releasesReleaseComparatorOrder).isEqualTo(releasesExpectedOrder);
    }

    @Test
    void compareTo_isSortingAsExpected_nested() {
        List<ReleaseEntity> releasesRandomOrder;
        do {
            releasesRandomOrder = getRandomizedOrderNested(releasesExpectedOrder);
        } while (Objects.equals(releasesRandomOrder, releasesExpectedOrder));

        List<ReleaseEntity> releasesNullFirstNaturalOrder = ReleaseEntitySorter.sorted(
                releasesRandomOrder.stream(),
                Comparator.nullsFirst(Comparator.naturalOrder()),
                Comparator.nullsFirst(Comparator.naturalOrder())
        ).collect(Collectors.toList());
        List<ReleaseEntity> releasesReleaseComparatorOrder = ReleaseEntitySorter.sorted(
                releasesRandomOrder.stream(),
                Comparator.nullsFirst(ReleaseEntity.RELEASE_COMPARATOR),
                Comparator.nullsFirst(ApplicationEntity.APPLICATION_COMPARATOR)
        ).collect(Collectors.toList());

        assertThat(releasesNullFirstNaturalOrder).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertThat(releasesReleaseComparatorOrder).isInOrder(ReleaseEntity.RELEASE_COMPARATOR);

        releasesNullFirstNaturalOrder.forEach(release -> {
            List<ApplicationEntity> applications =
                    release == null || release.getApplications() == null
                            ? List.of()
                            : release.getApplications();
            assertThat(applications).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        });
        releasesReleaseComparatorOrder.forEach(release -> {
            List<ApplicationEntity> applications =
                    release == null || release.getApplications() == null
                            ? List.of()
                            : release.getApplications();
            assertThat(applications).isInOrder(ApplicationEntity.APPLICATION_COMPARATOR);
        });

        assertThat(releasesNullFirstNaturalOrder).isEqualTo(releasesExpectedOrder);
        assertThat(releasesReleaseComparatorOrder).isEqualTo(releasesExpectedOrder);
    }
}
