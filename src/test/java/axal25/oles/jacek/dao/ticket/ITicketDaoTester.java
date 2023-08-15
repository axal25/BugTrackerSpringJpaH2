package axal25.oles.jacek.dao.ticket;

import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.TicketEntityFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
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

    Class<?> getTestClass();

    default void updateTicket_updatesDescriptionAndTitle() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "updateTicket_updatesDescriptionAndTitle",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        String descriptionPrefix = inputTicket.getDescription();
        String titlePrefix = inputTicket.getTitle();

        inputTicket.setDescription(descriptionPrefix + " before update");
        inputTicket.setTitle(titlePrefix + " before update");

        addAndAssertTicket(inputTicket);

        inputTicket.setDescription(descriptionPrefix + " after update");
        inputTicket.setTitle(titlePrefix + " after update");

        getTicketDao().updateTicket(inputTicket);

        TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());

        assertThat(retrievedTicket.getDescription()).isEqualTo(descriptionPrefix + " after update");
        assertThat(retrievedTicket.getTitle()).isEqualTo(titlePrefix + " after update");
    }

    default void closeTicket_updatesStatusToResolvedValue() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "closeTicket_updatesStatusToResolvedValue",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        inputTicket.setStatus("Not-Resolved");
        addAndAssertTicket(inputTicket);

        getTicketDao().closeTicketById(inputTicket.getId());

        TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());
        assertThat(retrievedTicket.getStatus()).isEqualTo("Resolved");
    }

    default void deleteTicket_deletesAddedTicket() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "deleteTicket_deletesAddedTicket",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        addAndAssertTicket(inputTicket);

        getTicketDao().deleteTicketById(inputTicket.getId());

        TicketEntity retrievedTicket = getTicketDao().getTicketById(inputTicket.getId());
        assertThat(retrievedTicket).isNull();
    }

    default void getAllTickets_containsAddedTicket_skippingLazilyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getAllTickets_containsAddedTicket_skippingLazilyFetchedEntities",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        addAndAssertTicket(inputTicket);

        List<TicketEntity> allTickets = getTicketDao().getAllTickets();

        allTickets = allTickets.stream()
                .map(ticket -> ticket.toBuilder()
                        .release(ticket.getRelease() == null
                                ? null
                                : ticket.getRelease().toBuilder()
                                .applications(null)
                                .build())
                        .build())
                .collect(Collectors.toList());
        inputTicket = inputTicket.toBuilder()
                .release(inputTicket.getRelease() == null
                        ? null
                        : inputTicket.getRelease().toBuilder()
                        .applications(null)
                        .build())
                .build();

        assertThat(allTickets).containsAtLeastElementsIn(List.of(inputTicket));
    }

    default void getAllTicketsEagerly_containsAddedTicket_includingLazilyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getAllTicketsEagerly_containsAddedTicket_includingLazilyFetchedEntities",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);
        addAndAssertTicket(inputTicket);

        List<TicketEntity> allTickets = getTicketDao().getAllTicketsEagerly();

        assertThat(allTickets.stream()
                .map(TicketEntity::toClonedComparableFullyFetchedEntity)
                .collect(Collectors.toList()))
                .containsAtLeastElementsIn(List.of(inputTicket.toClonedComparableFullyFetchedEntity()));
    }

    default void getTicketByIdEagerly_IsEqualToAddedTicket_includingLazilyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getTicketByIdEagerly_IsEqualToAddedTicket_includingLazilyFetchedEntities",
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        addAndAssertTicket(inputTicket);

        TicketEntity retrievedTicket = getTicketDao().getTicketByIdEagerly(inputTicket.getId());

        assertThat(inputTicket.toClonedComparableFullyFetchedEntity())
                .isEqualTo(retrievedTicket.toClonedComparableFullyFetchedEntity());
    }

    default void getTicketById_isEqualToAddedTicket_skippingLazilyFetchedEntities() {
        TicketEntity inputTicket = TicketEntityFactory.produce(
                "getTicketById_isEqualToAddedTicket_skippingLazilyFetchedEntities",
                getTestClass(),
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
                getTestClass(),
                null,
                null,
                null,
                EntityFactory.IdGenerateMode.NULL);

        addAndAssertTicket(inputTicket);
    }

    private void addAndAssertTicket(TicketEntity input) {
        TicketEntity comparableUnmanaged = input.toClonedComparableFullyFetchedEntity();
        hasNoIds(input);

        TicketEntity inserted = getTicketDao().addTicket(input);

        hasIds(inserted);
        hasIds(input);
        assertThat(inserted).isEqualTo(input);
        hasNoIds(comparableUnmanaged);
        assertEqualsIgnoringIds(input, comparableUnmanaged);
    }

    private void hasNoIds(TicketEntity ticket) {
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isNull();
        assertThat(ticket.getApplication()).isNotNull();
        assertThat(ticket.getApplication().getId()).isNull();
        assertThat(ticket.getRelease()).isNotNull();
        assertThat(ticket.getRelease().getId()).isNull();
        ticket.getRelease().getApplications()
                .forEach(application -> assertThat(application.getId()).isNull());
        assertThat(ticket.getRelease().getApplications())
                .containsAtLeastElementsIn(List.of(ticket.getApplication()));
    }

    private void hasIds(TicketEntity ticket) {
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isNotNull();
        assertThat(ticket.getApplication()).isNotNull();
        assertThat(ticket.getApplication().getId()).isNotNull();
        assertThat(ticket.getRelease()).isNotNull();
        assertThat(ticket.getRelease().getId()).isNotNull();
        ticket.getRelease().getApplications()
                .forEach(application -> assertThat(application.getId()).isNotNull());
        assertThat(ticket.getRelease().getApplications())
                .containsAtLeastElementsIn(List.of(ticket.getApplication()));
    }

    private void assertEqualsIgnoringIds(
            TicketEntity addedManaged,
            TicketEntity comparableUnmanaged) {
        // .toClonedComparableFullyFetchedEntity()
        // needs to be re-sorted because it got newly issued ids
        assertThat(comparableUnmanaged)
                .isEqualTo(addedManaged.toBuilder()
                        .id(null)
                        .application(addedManaged.getApplication().toBuilder()
                                .id(null)
                                .build())
                        .release(addedManaged.getRelease().toBuilder()
                                .id(null)
                                .applications(addedManaged.getRelease().getApplications().stream()
                                        .map(application -> application.toBuilder()
                                                .id(null)
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .build()
                        .toClonedComparableFullyFetchedEntity());
    }
}
