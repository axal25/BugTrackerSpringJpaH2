package axal25.oles.jacek.entity;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.json.JsonObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = Constants.Tables.RELEASES)
public class ReleaseEntity implements JsonObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.Formatters.PATTERN_DATE_TIME_FORMATTER)
    private LocalDate releaseDate;

    private String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = Constants.Tables.APPLICATIONS_TO_RELEASES,
            joinColumns = @JoinColumn(name = Constants.Tables.ApplicationsToReleases.RELEASE_ID),
            inverseJoinColumns = @JoinColumn(name = Constants.Tables.ApplicationsToReleases.APPLICATION_ID))
    private List<ApplicationEntity> applications = new ArrayList<>();
}
