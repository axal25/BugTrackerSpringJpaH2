package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

public class ApplicationEntityTest {

    public static ApplicationEntity getDeepCopy(ApplicationEntity inputApplication) {
        ApplicationEntity deepCopyApplication = new ApplicationEntity();

        deepCopyApplication.setId(inputApplication.getId());
        deepCopyApplication.setName(inputApplication.getName());
        deepCopyApplication.setDescription(inputApplication.getDescription());
        deepCopyApplication.setOwner(inputApplication.getOwner());

        return deepCopyApplication;
    }

    public static List<ApplicationEntity> getRandomizedOrder(List<ApplicationEntity> apps) {
        if (apps == null) {
            return null;
        }
        List<ApplicationEntity> shuffled = new ArrayList<>(apps);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    @Test
    public void deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal() {
        ApplicationEntity input = ApplicationEntityFactory.produce(
                "deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.RANDOM);
        ApplicationEntity expected = getDeepCopy(input);

        ApplicationEntity actual = input.deepCopy();

        assertThat(actual).isEqualTo(input);
        assertThat(actual).isEqualTo(expected);

        actual = input.deepCopy();
        actual.setId(actual.getId() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setName(actual.getName() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setOwner(actual.getOwner() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setDescription(actual.getDescription() + 1);
        assertThat(actual).isNotEqualTo(input);
    }

    @Test
    void compareTo_isSortingAsExpected() {
        List<ApplicationEntity> appsExpectedOrder = Stream.of(
                null,
                new ApplicationEntity(),

                ApplicationEntity.builder()
                        .owner("")
                        .build(),
                ApplicationEntity.builder()
                        .owner("1")
                        .build(),
                ApplicationEntity.builder()
                        .owner("2")
                        .build(),

                ApplicationEntity.builder()
                        .description("")
                        .build(),
                ApplicationEntity.builder()
                        .description("1")
                        .build(),
                ApplicationEntity.builder()
                        .description("2")
                        .build(),
                ApplicationEntity.builder()
                        .description("2")
                        .owner("")
                        .build(),
                ApplicationEntity.builder()
                        .description("2")
                        .owner("2")
                        .build(),

                ApplicationEntity.builder()
                        .name("")
                        .build(),
                ApplicationEntity.builder()
                        .name("1")
                        .build(),
                ApplicationEntity.builder()
                        .name("2")
                        .build(),
                ApplicationEntity.builder()
                        .name("2")
                        .description("")
                        .build(),
                ApplicationEntity.builder()
                        .name("2")
                        .description("2")
                        .build(),
                ApplicationEntity.builder()
                        .name("2")
                        .description("2")
                        .owner("")
                        .build(),
                ApplicationEntity.builder()
                        .name("2")
                        .description("2")
                        .owner("2")
                        .build(),

                ApplicationEntity.builder()
                        .id(1)
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("1")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("2")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("2")
                        .description("")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("2")
                        .description("2")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("2")
                        .description("2")
                        .owner("")
                        .build(),
                ApplicationEntity.builder()
                        .id(2)
                        .name("2")
                        .description("2")
                        .owner("2")
                        .build()
        ).collect(Collectors.toList());

        List<ApplicationEntity> appsRandomOrder;
        do {
            appsRandomOrder = getRandomizedOrder(appsExpectedOrder);
        } while (Objects.equals(appsRandomOrder, appsExpectedOrder));

        List<ApplicationEntity> appsNullFirstNaturalOrder = appsRandomOrder.stream()
                .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                .collect(Collectors.toList());
        assertThat(appsNullFirstNaturalOrder).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertThat(appsNullFirstNaturalOrder).isEqualTo(appsExpectedOrder);

        List<ApplicationEntity> appsApplicationComparatorOrder = appsRandomOrder.stream()
                .sorted(ApplicationEntity.APPLICATION_COMPARATOR)
                .collect(Collectors.toList());
        assertThat(appsNullFirstNaturalOrder).isInOrder(ApplicationEntity.APPLICATION_COMPARATOR);
        assertThat(appsApplicationComparatorOrder).isEqualTo(appsExpectedOrder);
    }
}
