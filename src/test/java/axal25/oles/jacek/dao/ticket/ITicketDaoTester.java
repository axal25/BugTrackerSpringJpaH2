package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.dao.DaoTestHelper;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public interface ITicketDaoTester {
    List<Method> REQUIRED_METHODS = Arrays.stream(ITicketDaoTester.class.getMethods())
            .filter(method -> !method.getName().equals("getTicketDao")
                    && !method.getName().equals("getDaoTestHelper")
                    && !method.getName().equals("validateAllTestMethodsAreImplementedAndAnnotated"))
            .collect(Collectors.toList());

    static void validateAllTestMethodsAreImplementedAndAnnotated(Class<?> toBeValidatedClass) {
        List<Method> toBeValidatedMethods = Arrays.asList(toBeValidatedClass.getMethods());

        List<Method> implementedMethods = toBeValidatedMethods.stream()
                .filter(toBeValidatedMethod ->
                        REQUIRED_METHODS.stream().anyMatch(requiredMethod ->
                                toBeValidatedMethod.getName().equals(requiredMethod.getName())))
                .collect(Collectors.toList());
        if (implementedMethods.size() == toBeValidatedMethods.size()) {
            List<Method> unimplementedMethods = REQUIRED_METHODS.stream()
                    .filter(requiredMethod ->
                            implementedMethods.stream().noneMatch(implementedMethod ->
                                    requiredMethod.getName().equals(implementedMethod.getName())))
                    .collect(Collectors.toList());
            throw new RuntimeException(toBeValidatedClass.getSimpleName() +
                    " has unimplemented methods required by " +
                    ITicketDaoTester.class.getSimpleName() +
                    ": [\r\n" +
                    unimplementedMethods.stream()
                            .map(method -> "\t" + method.toString())
                            .collect(Collectors.joining(",\r\n")) +
                    "\n\r].");
        }

        List<Method> implementedUnAnnotatedMethods = implementedMethods.stream()
                .filter(implementedMethod -> implementedMethod.getAnnotation(Test.class) == null)
                .collect(Collectors.toList());
        if (!implementedUnAnnotatedMethods.isEmpty()) {
            throw new RuntimeException(toBeValidatedClass.getSimpleName() +
                    " has implemented but not annotated with " +
                    Test.class +
                    " methods: [\r\n" +
                    implementedUnAnnotatedMethods.stream()
                            .map(method -> "\t" + method.toString())
                            .collect(Collectors.joining(",\r\n")) +
                    "\n\r].");
        }
    }

    ITicketDao getTicketDao();

    DaoTestHelper getDaoTestHelper();

    default void updateTicket() {

    }

    default void closeTicket() {

    }

    default void deleteTicket_deletesAddedTicket() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "deleteTicket_deletesAddedTicket",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        getDaoTestHelper().wrapInTransaction(entityManager -> {
            addAndAssertTicket(inputTicket);
            getTicketByIdAndAssertIsEqualTo(inputTicket);

            getTicketDao().deleteTicketById(inputTicket.getId());

            TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());
            assertThat(retrievedTicket).isNull();
        });
    }

    default void getAllTickets_containsAddedTicket() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getAllTickets_containsAddedTicket",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        AtomicReference<List<TicketEntity>> allTicketsReference = new AtomicReference<>();

        getDaoTestHelper().wrapInTransaction(entityManager -> {
            addAndAssertTicket(inputTicket);
            List<TicketEntity> allTickets = getTicketDao().getAllTickets();
            allTickets.forEach(ticket -> {
                boolean unusedToFetchLazyInitializations = ticket.getRelease().getApplications().isEmpty();
            });
            allTicketsReference.set(allTickets);
        });

        assertThat(allTicketsReference.get()).containsAtLeastElementsIn(List.of(inputTicket));
    }

    default void getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getTicketById_IsEqualToAddedTicket_includingLazyFetchedEntities",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        addAndAssertTicket(inputTicket);
        getDaoTestHelper().wrapInTransaction(entityManager -> {
            getTicketByIdAndAssertIsEqualTo(inputTicket);
        });
    }

    default void getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getTicketById_isEqualToAddedTicket_skippingLazyFetchedEntities",
                TicketCrudRepositoryTest.class,
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        addAndAssertTicket(inputTicket);

        TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());

        inputTicket.getRelease().setApplications(null);
        retrievedTicket.getRelease().setApplications(null);

        assertThat(inputTicket).isEqualTo(retrievedTicket);
    }

    default void addTicket_isSuccessfullyAdded() {
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

    private void getTicketByIdAndAssertIsEqualTo(TicketEntity inputTicket) {
        TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());
        assertThat(retrievedTicket).isNotNull();
        assertThat(retrievedTicket.getRelease()).isNotNull();

        assertThat(inputTicket.toComparableFullyFetchedEntity())
                .isEqualTo(retrievedTicket.toComparableFullyFetchedEntity());
    }

    private void addAndAssertTicket(TicketEntity inputTicket) {
        TicketEntity comparableTicket = inputTicket.toComparableFullyFetchedEntity();

        addAndAssertTicketIds(inputTicket);

        assertManagedAddedAndPojoComparableFullyFetchedTicketAreEqual(inputTicket, comparableTicket);
    }

    private void assertManagedAddedAndPojoComparableFullyFetchedTicketAreEqual(
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

    private void addAndAssertTicketIds(TicketEntity inputTicket) {
        assertThat(inputTicket).isNotNull();
        assertThat(inputTicket.getApplication()).isNotNull();
        assertThat(inputTicket.getRelease()).isNotNull();
        assertThat(inputTicket.getId()).isNull();
        assertThat(inputTicket.getApplication().getId()).isNull();
        assertThat(inputTicket.getRelease().getId()).isNull();

        TicketEntity inserted = getTicketDao().addTicket(inputTicket);

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
}
