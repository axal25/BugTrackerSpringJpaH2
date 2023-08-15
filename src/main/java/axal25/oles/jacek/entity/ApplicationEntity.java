package axal25.oles.jacek.entity;

import axal25.oles.jacek.json.JsonObject;
import lombok.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static axal25.oles.jacek.constant.Constants.Tables.APPLICATIONS;
import static axal25.oles.jacek.constant.Constants.Tables.Applications.ID;
import static axal25.oles.jacek.constant.Constants.Tables.Applications.NAME;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = APPLICATIONS)
public class ApplicationEntity implements JsonObject, Cloneable, CloneableComparableFullyFetchedEntity<ApplicationEntity> {
    public static final Comparator<ApplicationEntity> APPLICATION_COMPARATOR =
            Comparator.nullsFirst(
                    Comparator.comparing(
                                    ApplicationEntity::getId,
                                    Comparator.nullsFirst(Comparator.comparingInt(i -> i)))
                            .thenComparing(
                                    ApplicationEntity::getId,
                                    Comparator.nullsFirst(Comparator.comparingInt(i -> i)))
                            .thenComparing(
                                    ApplicationEntity::getName,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    ApplicationEntity::getDescription,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    ApplicationEntity::getOwner,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s))));
    public static final Comparator<List<ApplicationEntity>> APPLICATION_LIST_COMPARATOR =
            Comparator.nullsFirst(
                    Comparator.<List<ApplicationEntity>, Integer>comparing(
                                    List::size,
                                    Comparator.naturalOrder())
                            .thenComparing(
                                    Comparator.nullsFirst((apps1, apps2) -> {
                                        List<ApplicationEntity> appsSorted1 = apps1.stream()
                                                .sorted(APPLICATION_COMPARATOR)
                                                .collect(Collectors.toList());
                                        List<ApplicationEntity> appsSorted2 = apps2.stream()
                                                .sorted(APPLICATION_COMPARATOR)
                                                .collect(Collectors.toList());
                                        return IntStream.range(0, Math.max(appsSorted1.size(), appsSorted2.size()))
                                                .map(i -> APPLICATION_COMPARATOR
                                                        .compare(appsSorted1.get(i), appsSorted2.get(i)))
                                                .filter(compareToValue -> compareToValue != 0)
                                                .findFirst()
                                                .orElse(0);
                                    })));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ID)
    private Integer id;
    @Column(name = NAME, nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private String owner;

    @Override
    public ApplicationEntity toClonedComparableFullyFetchedEntity() {
        return deepCopy();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return deepCopy();
        }
    }

    public ApplicationEntity deepCopy() {
        return toBuilder()
                .build();
    }


    @Override
    public int compareTo(@Nonnull ApplicationEntity other) {
        return APPLICATION_COMPARATOR.compare(this, other);
    }
}
