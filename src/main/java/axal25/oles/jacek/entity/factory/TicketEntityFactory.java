package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.jdbc.id.provider.JdbcTicketEntityIdProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TicketEntityFactory implements EntityFactory<Integer> {
    private static final Random random = new Random();
    private static final TicketEntityFactory factory = new TicketEntityFactory();

    private TicketEntityFactory() {
    }

    public static TicketEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer ticketId,
            @Nullable ApplicationEntity application,
            @Nullable ReleaseEntity release,
            EntityFactory.IdGenerateMode idGenerateMode) {
        
        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (ticketId == null) {
            ticketId = factory.getId(ticketId, idGenerateMode);
        }

        if (application == null) {
            application = ApplicationEntityFactory
                    .produce(expectedMethodName, methodOwnerClass, null, idGenerateMode);
        }

        if (release == null) {
            release = ReleaseEntityFactory
                    .produce(expectedMethodName, methodOwnerClass, null, List.of(application), idGenerateMode);
        }

        TicketEntity ticket = new TicketEntity();
        ticket.setId(ticketId);
        ticket.setTitle(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "title",
                        methodName,
                        ticket.getId()));
        ticket.setDescription(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "description",
                        methodName,
                        ticket.getId()));
        ticket.setStatus(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "status",
                        methodName,
                        ticket.getId()));
        ticket.setApplication(application);
        ticket.setRelease(release);

        return ticket;
    }


    @Override
    public Integer getIdFromJdbc() {
        return JdbcTicketEntityIdProvider.generateId();
    }

    @Override
    public Integer generateRandomId() {
        return random.nextInt();
    }
}
