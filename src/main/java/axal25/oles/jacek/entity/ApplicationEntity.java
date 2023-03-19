package axal25.oles.jacek.entity;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.json.JsonObject;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = Constants.Tables.APPLICATIONS)
public class ApplicationEntity implements JsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Constants.Tables.Applications.ID)
    private Integer id;

    @Column(name = Constants.Tables.Applications.NAME, nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private String owner;

//    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
//    @JoinTable(
//            name = Constants.Tables.RELEASES_TO_APPLICATIONS,
//            joinColumns = @JoinColumn(name = "applications_application_id"),
//            inverseJoinColumns = @JoinColumn(name = "release_entity_id"))
//    private List<ReleaseEntity> releases = new ArrayList<>();
}
