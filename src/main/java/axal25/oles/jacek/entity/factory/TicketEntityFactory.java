package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.jdbc.JdbcTicketEntityIdProvider;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@AllArgsConstructor
public class TicketEntityFactory {

    public static TicketEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer ticketId,
            @Nullable ApplicationEntity application,
            @Nullable ReleaseEntity release) {
        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);
        FieldValueFormatter fieldValueFormatter = new FieldValueFormatter(TicketEntity.class);

        if (ticketId == null) {
            ticketId = JdbcTicketEntityIdProvider.generateId();
        }

        if (application == null) {
            application = ApplicationEntityFactory
                    .produce(expectedMethodName, methodOwnerClass, null);
        }

        if (release == null) {
            release = ReleaseEntityFactory
                    .produce(expectedMethodName, methodOwnerClass, null, List.of(application));
        }

        TicketEntity ticket = new TicketEntity();
        ticket.setId(ticketId);
        ticket.setTitle(
                fieldValueFormatter.getStringValue(
                        "title",
                        methodName,
                        ticket.getId()));
        ticket.setDescription(
                fieldValueFormatter.getStringValue(
                        "description",
                        methodName,
                        ticket.getId()));
        ticket.setStatus(
                fieldValueFormatter.getStringValue(
                        "status",
                        methodName,
                        ticket.getId()));
        ticket.setApplication(application);
        ticket.setRelease(release);

        return ticket;
    }


}
