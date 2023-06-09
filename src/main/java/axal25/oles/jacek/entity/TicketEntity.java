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
@Table(name = Constants.Tables.TICKETS)
public class TicketEntity implements JsonObject, ComparableFullyFetchedEntity<TicketEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String status;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = Constants.Tables.Tickets.APPLICATION_ID)
    private ApplicationEntity application;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = Constants.Tables.RELEASES_TO_TICKETS,
            joinColumns = @JoinColumn(name = Constants.Tables.ReleasesToTickets.TICKET_ID),
            inverseJoinColumns = @JoinColumn(name = Constants.Tables.ReleasesToTickets.RELEASE_ID))
    private ReleaseEntity release;

    @Override
    public TicketEntity toComparableFullyFetchedEntity() {
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
        TicketEntity comparableTicket = new TicketEntity();

        comparableTicket.setId(getId());
        comparableTicket.setTitle(getTitle());
        comparableTicket.setStatus(getStatus());
        comparableTicket.setDescription(getDescription());
        comparableTicket.setApplication(getApplication().deepCopy());
        comparableTicket.setRelease(getRelease().deepCopy());

        return comparableTicket;
    }
}
