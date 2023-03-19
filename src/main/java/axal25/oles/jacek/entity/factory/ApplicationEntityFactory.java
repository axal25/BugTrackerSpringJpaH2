package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.jdbc.JdbcApplicationEntityIdProvider;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;

@AllArgsConstructor
public class ApplicationEntityFactory {

    public static ApplicationEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer applicationId) {
        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);
        FieldValueFormatter fieldValueFormatter = new FieldValueFormatter(ApplicationEntity.class);

        if (applicationId == null) {
            applicationId = JdbcApplicationEntityIdProvider.generateId();
        }

        ApplicationEntity application = new ApplicationEntity();
        application.setId(applicationId);
        application.setName(
                fieldValueFormatter.getStringValue(
                        "name",
                        methodName,
                        application.getId()));
        application.setDescription(
                fieldValueFormatter.getStringValue(
                        "description",
                        methodName,
                        application.getId()));
        application.setOwner(
                fieldValueFormatter.getStringValue(
                        "owner",
                        methodName,
                        application.getId()));

        return application;
    }
}
