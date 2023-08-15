package axal25.oles.jacek.entity;

import axal25.oles.jacek.json.JsonObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static axal25.oles.jacek.constant.Constants.Formatters.PATTERN_DATE_TIME_FORMATTER;
import static axal25.oles.jacek.constant.Constants.Tables.APPLICATIONS_TO_RELEASES;
import static axal25.oles.jacek.constant.Constants.Tables.Applications.ORDER_BY;
import static axal25.oles.jacek.constant.Constants.Tables.ApplicationsToReleases.APPLICATION_ID;
import static axal25.oles.jacek.constant.Constants.Tables.ApplicationsToReleases.RELEASE_ID;
import static axal25.oles.jacek.constant.Constants.Tables.RELEASES;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = RELEASES)
public class ReleaseEntity implements JsonObject, CloneableComparableFullyFetchedEntity<ReleaseEntity> {
    public static final Comparator<ReleaseEntity> RELEASE_COMPARATOR =
            Comparator.nullsFirst(
                    Comparator.comparing(
                                    ReleaseEntity::getId,
                                    Comparator.nullsFirst(Comparator.comparingInt(i -> i)))
                            .thenComparing(
                                    ReleaseEntity::getDescription,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    ReleaseEntity::getReleaseDate,
                                    Comparator.nullsFirst(Comparator.comparing(d -> d)))
                            .thenComparing(
                                    ReleaseEntity::getApplications,
                                    ApplicationEntity.APPLICATION_LIST_COMPARATOR)
                            .thenComparing(
                                    ReleaseEntity::getApplications,
                                    Comparator.nullsFirst(
                                            Comparator.comparing(
                                                    List::size,
                                                    Comparator.comparingInt(i -> i)))));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonFormat(shape = STRING, pattern = PATTERN_DATE_TIME_FORMATTER)
    private LocalDate releaseDate;

    private String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = APPLICATIONS_TO_RELEASES,
            joinColumns = @JoinColumn(name = RELEASE_ID),
            inverseJoinColumns = @JoinColumn(name = APPLICATION_ID))
    @OrderBy(value = ORDER_BY)
    private List<ApplicationEntity> applications = new ArrayList<>();

    @Override
    public ReleaseEntity toClonedComparableFullyFetchedEntity() {
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

    public ReleaseEntity deepCopy() {
        return toBuilder()
                .applications(applications == null ? null : applications.stream()
                        .map(application -> application == null ? null : application.deepCopy())
                        .sorted(ApplicationEntity.APPLICATION_COMPARATOR)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public int compareTo(@Nonnull ReleaseEntity other) {
        return RELEASE_COMPARATOR.compare(this, other);
    }
}
