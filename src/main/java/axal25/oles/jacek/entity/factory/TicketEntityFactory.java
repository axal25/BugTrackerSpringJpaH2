package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.dao.ticket.DaoTicketEntityIdProvider;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.jdbc.id.provider.JdbcTicketEntityIdProvider;

import javax.annotation.Nullable;
import java.util.Random;

public class TicketEntityFactory implements EntityFactory<Integer> {
    private static final Random random = new Random();
    private static final TicketEntityFactory factory = new TicketEntityFactory();

    private TicketEntityFactory() {
    }

    public static TicketEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer id,
            @Nullable ApplicationEntity application,
            @Nullable ReleaseEntity release,
            EntityFactory.IdGenerateMode idGenerateMode) {

        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (id == null) {
            id = factory.getId(id, idGenerateMode);
        }

        if (release == null) {
            release = ReleaseEntityFactory
                    .produce(expectedMethodName, methodOwnerClass, null, null, idGenerateMode);
        }

        if (application == null) {
            application = release.getApplications().get(random.nextInt(release.getApplications().size()));
        }

        Integer idForNonIdFields = factory.getIdForNonIdFields(id, idGenerateMode);

        TicketEntity ticket = new TicketEntity();
        ticket.setId(id);
        ticket.setTitle(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "title",
                        methodName,
                        idForNonIdFields));
        ticket.setDescription(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "description",
                        methodName,
                        idForNonIdFields));
        ticket.setStatus(
                FieldValueFormatter.getStringValue(
                        TicketEntity.class,
                        "status",
                        methodName,
                        idForNonIdFields));
        ticket.setApplication(application);
        ticket.setRelease(release);

        return ticket;
    }


    @Override
    public Integer getIdFromJdbc() {
        return JdbcTicketEntityIdProvider.generateId();
    }

    @Override
    public Integer getIdFromEntityManager() {
        return DaoTicketEntityIdProvider.generateId();
    }

    @Override
    public Integer generateRandomId() {
        return random.nextInt();
    }
}
