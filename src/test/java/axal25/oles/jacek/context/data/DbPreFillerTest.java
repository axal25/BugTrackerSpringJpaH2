package axal25.oles.jacek.context.data;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.model.DbPreFillContainer;
import axal25.oles.jacek.service.application.ApplicationService;
import axal25.oles.jacek.service.release.ReleaseService;
import axal25.oles.jacek.service.ticket.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class DbPreFillerTest {

    @Autowired
    private DbPreFiller dbPreFiller;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private TicketService ticketService;

    @Test
    void prefillInMemDb_assertEveryTopLevelEntityWasInserted() {
        DbPreFillContainer container = dbPreFiller.generateContainer();
        container = container.toBuilder()
                .release(container.getRelease().toBuilder()
                        .applications(List.of())
                        .build())
                .ticket(container.getTicket().toBuilder()
                        .release(null)
                        .application(null)
                        .build())
                .build();

        dbPreFiller.prefillInMemDb(container);

        List<ApplicationEntity> allApps = applicationService.getAllApplications();
        List<ReleaseEntity> allReleases = releaseService.getAllReleasesEagerly()
                .stream().map(ReleaseEntity::toClonedComparableFullyFetchedEntity).collect(Collectors.toList());
        List<TicketEntity> allTickets = ticketService.getAllTicketsEagerly();
        assertThat(allApps).contains(container.getApplication());
        assertThat(allReleases).contains(container.getRelease().toClonedComparableFullyFetchedEntity());
        assertThat(allTickets).contains(container.getTicket());
    }

    @Test
    void prefillInMemDb_assertEverythingWasInserted() {
        DbPreFillContainer container = dbPreFiller.generateContainer();

        dbPreFiller.prefillInMemDb(container);

        List<ApplicationEntity> allApps = applicationService.getAllApplications()
                .stream().map(ApplicationEntity::toClonedComparableFullyFetchedEntity).collect(Collectors.toList());
        List<ReleaseEntity> allReleases = releaseService.getAllReleasesEagerly()
                .stream().map(ReleaseEntity::toClonedComparableFullyFetchedEntity).collect(Collectors.toList());
        List<TicketEntity> allTickets = ticketService.getAllTicketsEagerly()
                .stream().map(TicketEntity::toClonedComparableFullyFetchedEntity).collect(Collectors.toList());
        List<ApplicationEntity> containerApps = Stream.of(
                        Stream.of(container.getApplication()),
                        container.getRelease().getApplications().stream(),
                        Stream.of(container.getTicket().getApplication()),
                        container.getTicket().getRelease().getApplications().stream())
                .flatMap(apps -> apps)
                .map(ApplicationEntity::toClonedComparableFullyFetchedEntity)
                .distinct()
                .collect(Collectors.toList());
        List<ReleaseEntity> containerReleases = Stream.of(
                        container.getRelease(),
                        container.getTicket().getRelease())
                .map(ReleaseEntity::toClonedComparableFullyFetchedEntity)
                .distinct()
                .collect(Collectors.toList());
        TicketEntity containerTicket = container.getTicket().toClonedComparableFullyFetchedEntity();
        assertThat(allTickets).contains(containerTicket);
        assertThat(allReleases).containsAtLeastElementsIn(containerReleases);
        assertThat(allApps).containsAtLeastElementsIn(containerApps);
    }
}
