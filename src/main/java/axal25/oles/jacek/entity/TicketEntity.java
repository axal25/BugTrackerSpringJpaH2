package axal25.oles.jacek.entity;

import axal25.oles.jacek.json.JsonObject;
import lombok.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Comparator;

import static axal25.oles.jacek.constant.Constants.Tables.RELEASES_TO_TICKETS;
import static axal25.oles.jacek.constant.Constants.Tables.ReleasesToTickets.RELEASE_ID;
import static axal25.oles.jacek.constant.Constants.Tables.ReleasesToTickets.TICKET_ID;
import static axal25.oles.jacek.constant.Constants.Tables.TICKETS;
import static axal25.oles.jacek.constant.Constants.Tables.Tickets.APPLICATION_ID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = TICKETS)
public class TicketEntity implements JsonObject, CloneableComparableFullyFetchedEntity<TicketEntity> {
    public static final Comparator<TicketEntity> TICKET_COMPARATOR =
            Comparator.nullsFirst(
                    Comparator.comparing(
                                    TicketEntity::getId,
                                    Comparator.nullsFirst(Comparator.comparingInt(i -> i)))
                            .thenComparing(
                                    TicketEntity::getTitle,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    TicketEntity::getStatus,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    TicketEntity::getDescription,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    TicketEntity::getRelease,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s)))
                            .thenComparing(
                                    TicketEntity::getApplication,
                                    Comparator.nullsFirst(Comparator.comparing(s -> s))));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String status;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = APPLICATION_ID)
    private ApplicationEntity application;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = RELEASES_TO_TICKETS,
            joinColumns = @JoinColumn(name = TICKET_ID),
            inverseJoinColumns = @JoinColumn(name = RELEASE_ID))
    private ReleaseEntity release;

    @Override
    public TicketEntity toClonedComparableFullyFetchedEntity() {
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

    public TicketEntity deepCopy() {
        return toBuilder()
                .application(application == null ? null : application.deepCopy())
                .release(release == null ? null : release.deepCopy())
                .build();
    }

    @Override
    public int compareTo(@Nonnull TicketEntity other) {
        return TICKET_COMPARATOR.compare(this, other);
    }
}
