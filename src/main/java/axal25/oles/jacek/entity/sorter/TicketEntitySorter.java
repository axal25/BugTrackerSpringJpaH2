package axal25.oles.jacek.entity.sorter;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.TicketEntity;

import java.util.Comparator;
import java.util.stream.Stream;

public class TicketEntitySorter {

    public static Stream<TicketEntity> sorted(
            Stream<TicketEntity> tickets,
            Comparator<? super TicketEntity> ticketComparator,
            Comparator<? super ApplicationEntity> applicationComparator) {
        return tickets == null ? null : tickets.map(ticket -> sorted(ticket, applicationComparator))
                .sorted(ticketComparator);
    }

    public static TicketEntity sorted(TicketEntity ticket, Comparator<? super ApplicationEntity> applicationComparator) {
        return ticket == null ? null
                : ticket.getRelease() == null ? ticket
                : ticket.toBuilder()
                .release(ReleaseEntitySorter.sorted(ticket.getRelease(), applicationComparator))
                .build();
    }
}
