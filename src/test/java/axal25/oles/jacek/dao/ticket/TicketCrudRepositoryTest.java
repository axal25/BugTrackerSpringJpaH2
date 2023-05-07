package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.dao.DaoTestHelper;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import axal25.oles.jacek.jdbc.DatabaseUtils;
import axal25.oles.jacek.jdbc.dao.JdbcApplicationDao;
import axal25.oles.jacek.jdbc.dao.JdbcReleaseDao;
import axal25.oles.jacek.jdbc.dao.JdbcTicketDao;
import axal25.oles.jacek.json.JsonObject;
import axal25.oles.jacek.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class TicketCrudRepositoryTest {

    @Autowired
    @Qualifier("SpringCrudRepository")
    private ITicketDao ticketDao;

    @Autowired
    private DaoTestHelper daoTestHelper;

    @Test
    void updateTicket() {

    }

    @Test
    void closeTicket() {

    }

    @Test
    public void deleteTicket_deletesAddedTicket() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "deleteTicket_deletesAddedTicket",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        inputTicket.setId(null);
        inputTicket.getApplication().setId(null);
        inputTicket.getRelease().setId(null);

        daoTestHelper.wrapInTransaction(entityManager -> {
            addAndAssertTicket(inputTicket);
            getTicketByIdAndAssertIsEqualTo(inputTicket);

            ticketDao.deleteTicketById(inputTicket.getId());

            TicketEntity retrievedTicket = ticketDao.getTicketById(inputTicket.getId());
            assertThat(retrievedTicket).isNull();
        });
    }

    @Test
    public void getAllTickets_containsAddedTicket() {
        FIHandler fiHandler = () -> {

            TicketEntity inputTicket = TicketEntityFactory.produce(
                    "getAllTickets_containsAddedTicket",
                    TicketCrudRepositoryTest.class,
                    null,
                    null,
                    null,
                    EntityFactory.IdGenerateMode.NULL);
            inputTicket.setId(null);
            inputTicket.getApplication().setId(null);
            inputTicket.getRelease().setId(null);

            AtomicReference<List<TicketEntity>> allTicketsReference = new AtomicReference<>();

            daoTestHelper.wrapInTransaction(entityManager -> {
                addAndAssertTicket(inputTicket);
                List<TicketEntity> allTickets = ticketDao.getAllTickets();
                allTickets.forEach(ticket -> {
                    boolean unusedToFetchLazyInitializations = ticket.getRelease().getApplications().isEmpty();
                });
                allTicketsReference.set(allTickets);
            });

            assertThat(allTicketsReference.get()).containsAtLeastElementsIn(List.of(inputTicket));
        };

        fiHandler.handle();
    }

    @Test
    public void getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities() {
        FIHandler fiHandler = () -> {
            TicketEntity inputTicket = TicketEntityFactory.produce(
                    "getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities",
                    TicketCrudRepositoryTest.class,
                    null,
                    null,
                    null,
                    EntityFactory.IdGenerateMode.NULL);
            inputTicket.setId(null);
            inputTicket.getApplication().setId(null);
            inputTicket.getRelease().setId(null);

            addAndAssertTicket(inputTicket);
            daoTestHelper.wrapInTransaction(entityManager -> {
                getTicketByIdAndAssertIsEqualTo(inputTicket);
            });
        };

        fiHandler.handle();
    }

    public void getTicketByIdAndAssertIsEqualTo(TicketEntity inputTicket) {
        TicketEntity retrievedTicket = ticketDao.getTicketById(inputTicket.getId());
        assertThat(retrievedTicket).isNotNull();
        assertThat(retrievedTicket.getRelease()).isNotNull();

        assertThat(inputTicket.toComparableFullyFetchedEntity())
                .isEqualTo(retrievedTicket.toComparableFullyFetchedEntity());
    }

    @Test
    public void getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        inputTicket.setId(null);
        inputTicket.getApplication().setId(null);
        inputTicket.getRelease().setId(null);

        addAndAssertTicket(inputTicket);

        TicketEntity retrievedTicket = ticketDao.getTicketById(inputTicket.getId());

        inputTicket.getRelease().setApplications(null);
        retrievedTicket.getRelease().setApplications(null);

        assertThat(inputTicket).isEqualTo(retrievedTicket);
    }

    @Test
    public void addTicket_isSuccessfullyAdded() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "addTicket_isSuccessfullyAdded",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        inputTicket.setId(null);
        inputTicket.getApplication().setId(null);
        inputTicket.getRelease().setId(null);

        addAndAssertTicket(inputTicket);
    }

    private void addAndAssertTicket(TicketEntity inputTicket) {
        TicketEntity comparableTicket = inputTicket.toComparableFullyFetchedEntity();

        addAndAssertTicketIds(inputTicket);

        assertManagedAddedAndPojoComparableFullyFetchedTicketAreEqual(inputTicket, comparableTicket);
    }

    public void assertManagedAddedAndPojoComparableFullyFetchedTicketAreEqual(
            TicketEntity addedManagedTicket,
            TicketEntity comparableTicket) {
        // fill Ids on comparable
        comparableTicket.setId(addedManagedTicket.getId());
        comparableTicket.getApplication().setId(addedManagedTicket.getApplication().getId());
        comparableTicket.getRelease().setId(addedManagedTicket.getRelease().getId());
        comparableTicket.getRelease().getApplications().forEach(comparableApp -> {
            List<ApplicationEntity> matchingInputApps = addedManagedTicket.getRelease()
                    .getApplications().stream().filter(inputApp ->
                            StringUtils.equals(comparableApp.getName(), inputApp.getName())
                                    && StringUtils.equals(comparableApp.getDescription(), inputApp.getDescription())
                                    && StringUtils.equals(comparableApp.getOwner(), inputApp.getOwner()))
                    .collect(Collectors.toList());
            if (matchingInputApps.size() != 1) {
                throw new NoSuchElementException("Could not find matching " +
                        ApplicationEntity.class.getSimpleName() +
                        "s. Comparable " +
                        ApplicationEntity.class.getSimpleName() +
                        ": " +
                        comparableApp +
                        ", Matching Added Managed " +
                        ApplicationEntity.class.getSimpleName() +
                        " [" +
                        matchingInputApps.stream().map(Object::toString).collect(Collectors.joining(", ")) +
                        "].");
            }
            comparableApp.setId(matchingInputApps.get(0).getId());
        });

        assertThat(comparableTicket).isEqualTo(addedManagedTicket.toComparableFullyFetchedEntity());
    }

    public void addAndAssertTicketIds(TicketEntity inputTicket) {
        assertThat(inputTicket).isNotNull();
        assertThat(inputTicket.getApplication()).isNotNull();
        assertThat(inputTicket.getRelease()).isNotNull();
        assertThat(inputTicket.getId()).isNull();
        assertThat(inputTicket.getApplication().getId()).isNull();
        assertThat(inputTicket.getRelease().getId()).isNull();

        TicketEntity inserted = ticketDao.addTicket(inputTicket);

        assertThat(inserted).isNotNull();
        assertThat(inserted).isEqualTo(inputTicket);

        assertThat(inserted.getId()).isNotNull();
        assertThat(inserted.getApplication().getId()).isNotNull();
        assertThat(inserted.getRelease().getId()).isNotNull();

        inserted.getRelease().getApplications().forEach(application ->
                assertThat(application.getId()).isNotNull());
        assertThat(inserted.getRelease().getApplications())
                .containsAtLeastElementsIn(List.of(inserted.getApplication()));
    }

    public interface FIHandler {
        void toBeHandled();

        default void handle() {
            try {
                toBeHandled();
            } catch (DataIntegrityViolationException e) {
                printApps();
                printReleases();
                printTickets();
                throw e;
            }
        }

        private void printApps() {
            List<ApplicationEntity> apps;

            try {
                apps = JdbcApplicationDao.selectApplications(DatabaseUtils.getConnection());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("apps: " + CollectionUtils.lengthyElementsToString(apps.stream()
                    .map(JsonObject::toJsonPrettyString)
                    .collect(Collectors.toList())));
        }

        private void printReleases() {
            List<ReleaseEntity> releases;

            try {
                releases = JdbcReleaseDao.selectReleases(DatabaseUtils.getConnection());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("releases: " + CollectionUtils.lengthyElementsToString(releases.stream()
                    .map(JsonObject::toJsonPrettyString)
                    .collect(Collectors.toList())));
        }

        private void printTickets() {
            List<TicketEntity> tickets;

            try {
                tickets = JdbcTicketDao.selectTickets(DatabaseUtils.getConnection());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("tickets: " + CollectionUtils.lengthyElementsToString(tickets.stream()
                    .map(JsonObject::toJsonPrettyString)
                    .collect(Collectors.toList())));
        }
    }
}
