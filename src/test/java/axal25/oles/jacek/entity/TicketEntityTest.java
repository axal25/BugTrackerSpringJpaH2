package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import axal25.oles.jacek.entity.sorter.TicketEntitySorter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

public class TicketEntityTest {
    private static final List<TicketEntity> ticketsExpectedOrder = Stream.of(
            null,
            new TicketEntity(),

            TicketEntity.builder()
                    .application(new ApplicationEntity())
                    .build(),

            TicketEntity.builder()
                    .release(new ReleaseEntity())
                    .build(),
            TicketEntity.builder()
                    .release(new ReleaseEntity())
                    .application(new ApplicationEntity())
                    .build(),
            TicketEntity.builder()
                    .release(ReleaseEntity.builder()
                            .applications(List.of(
                                    new ApplicationEntity(),
                                    ApplicationEntity.builder().id(1).build(),
                                    ApplicationEntity.builder().id(2).build(),
                                    ApplicationEntity.builder().id(3).build(),
                                    ApplicationEntity.builder().id(4).build(),
                                    ApplicationEntity.builder().id(5).build()

                            ))
                            .build())
                    .build(),

            TicketEntity.builder()
                    .description("")
                    .build(),
            TicketEntity.builder()
                    .description("1")
                    .build(),
            TicketEntity.builder()
                    .description("2")
                    .build(),
            TicketEntity.builder()
                    .description("2")
                    .release(new ReleaseEntity())
                    .build(),
            TicketEntity.builder()
                    .description("2")
                    .release(new ReleaseEntity())
                    .application(new ApplicationEntity())
                    .build(),

            TicketEntity.builder()
                    .status("")
                    .build(),
            TicketEntity.builder()
                    .status("1")
                    .build(),
            TicketEntity.builder()
                    .status("2")
                    .build(),
            TicketEntity.builder()
                    .status("2")
                    .description("")
                    .build(),
            TicketEntity.builder()
                    .status("2")
                    .description("2")
                    .build(),
            TicketEntity.builder()
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .build(),
            TicketEntity.builder()
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .application(new ApplicationEntity())
                    .build(),

            TicketEntity.builder()
                    .title("")
                    .build(),
            TicketEntity.builder()
                    .title("1")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("2")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("2")
                    .description("")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("2")
                    .description("2")
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .build(),
            TicketEntity.builder()
                    .title("2")
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .application(new ApplicationEntity())
                    .build(),

            TicketEntity.builder()
                    .id(1)
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("2")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("2")
                    .description("")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("2")
                    .description("2")
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .build(),
            TicketEntity.builder()
                    .id(2)
                    .title("2")
                    .status("2")
                    .description("2")
                    .release(new ReleaseEntity())
                    .application(new ApplicationEntity())
                    .build()
    ).collect(Collectors.toList());

    public static TicketEntity getDeepCopy(TicketEntity inputTicket) {
        TicketEntity deepCopyTicket = new TicketEntity();

        deepCopyTicket.setId(inputTicket.getId());
        deepCopyTicket.setTitle(inputTicket.getTitle());
        deepCopyTicket.setStatus(inputTicket.getStatus());
        deepCopyTicket.setDescription(inputTicket.getDescription());
        deepCopyTicket.setApplication(inputTicket.getApplication().deepCopy());
        deepCopyTicket.setRelease(inputTicket.getRelease().deepCopy());

        return deepCopyTicket;
    }

    public static List<TicketEntity> getRandomizedOrderNested(List<TicketEntity> tickets) {
        if (tickets == null) {
            return null;
        }
        return getRandomizedOrderTopLayerOnly(tickets).stream()
                .map(TicketEntityTest::getRandomizedOrderNested)
                .collect(Collectors.toList());
    }

    private static List<TicketEntity> getRandomizedOrderTopLayerOnly(List<TicketEntity> tickets) {
        List<TicketEntity> shuffled = new ArrayList<>(tickets);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static TicketEntity getRandomizedOrderNested(TicketEntity ticket) {
        return ticket == null ? null : ticket.toBuilder()
                .release(ReleaseEntityTest.getRandomizedOrderNested(ticket.getRelease()))
                .build();
    }

    @Test
    public void deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal() {
        TicketEntity input = TicketEntityFactory.produce(
                "deepCopy_isEqualToExpected_copyModificationsDoNoAffectOriginal",
                getClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.RANDOM);
        TicketEntity expected = getDeepCopy(input);

        TicketEntity actual = input.deepCopy();

        assertThat(actual).isEqualTo(input);
        assertThat(actual).isEqualTo(expected);

        actual = input.deepCopy();
        actual.setId(actual.getId() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setStatus(actual.getStatus() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setTitle(actual.getTitle() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setDescription(actual.getDescription() + 1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setApplication(null);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.getApplication().setId(1);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.setRelease(null);
        assertThat(actual).isNotEqualTo(input);

        actual = input.deepCopy();
        actual.getRelease().setId(1);
        assertThat(actual).isNotEqualTo(input);
    }

    @Test
    void compareTo_isSortingAsExpected_topLayerOnly() {
        List<TicketEntity> ticketsRandomOrder;
        do {
            ticketsRandomOrder = getRandomizedOrderTopLayerOnly(ticketsExpectedOrder);
        } while (Objects.equals(ticketsRandomOrder, ticketsExpectedOrder));

        List<TicketEntity> ticketsNullFirstNaturalOrder = ticketsRandomOrder.stream()
                .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                .collect(Collectors.toList());
        List<TicketEntity> ticketsTicketComparatorOrder = ticketsRandomOrder.stream()
                .sorted(TicketEntity.TICKET_COMPARATOR)
                .collect(Collectors.toList());

        assertThat(ticketsNullFirstNaturalOrder).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertThat(ticketsTicketComparatorOrder).isInOrder(TicketEntity.TICKET_COMPARATOR);

        assertThat(ticketsNullFirstNaturalOrder).isEqualTo(ticketsExpectedOrder);
        assertThat(ticketsTicketComparatorOrder).isEqualTo(ticketsExpectedOrder);
    }

    @Test
    void compareTo_isSortingAsExpected_nested() {
        List<TicketEntity> ticketsRandomOrder;
        do {
            ticketsRandomOrder = getRandomizedOrderNested(ticketsExpectedOrder);
        } while (Objects.equals(ticketsRandomOrder, ticketsExpectedOrder));

        List<TicketEntity> ticketsNullFirstNaturalOrder = TicketEntitySorter.sorted(
                        ticketsRandomOrder.stream(),
                        Comparator.nullsFirst(Comparator.naturalOrder()),
                        Comparator.nullsFirst(Comparator.naturalOrder()))
                .collect(Collectors.toList());
        List<TicketEntity> ticketsTicketComparatorOrder = TicketEntitySorter.sorted(
                        ticketsRandomOrder.stream(),
                        TicketEntity.TICKET_COMPARATOR,
                        ApplicationEntity.APPLICATION_COMPARATOR)
                .collect(Collectors.toList());

        assertThat(ticketsNullFirstNaturalOrder).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertThat(ticketsTicketComparatorOrder).isInOrder(TicketEntity.TICKET_COMPARATOR);

        ticketsNullFirstNaturalOrder.forEach(ticket -> {
            List<ApplicationEntity> applications =
                    ticket == null || ticket.getRelease() == null || ticket.getRelease().getApplications() == null
                            ? List.of()
                            : ticket.getRelease().getApplications();
            assertThat(applications).isInOrder(Comparator.nullsFirst(Comparator.naturalOrder()));
        });
        ticketsTicketComparatorOrder.forEach(ticket -> {
            List<ApplicationEntity> applications =
                    ticket == null || ticket.getRelease() == null || ticket.getRelease().getApplications() == null
                            ? List.of()
                            : ticket.getRelease().getApplications();
            assertThat(applications).isInOrder(ApplicationEntity.APPLICATION_COMPARATOR);
        });

        assertThat(ticketsNullFirstNaturalOrder).isEqualTo(ticketsExpectedOrder);
        assertThat(ticketsTicketComparatorOrder).isEqualTo(ticketsExpectedOrder);
    }
}
