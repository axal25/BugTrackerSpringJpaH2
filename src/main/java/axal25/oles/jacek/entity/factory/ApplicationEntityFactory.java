package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.jdbc.id.provider.JdbcApplicationEntityIdProvider;

import javax.annotation.Nullable;
import java.util.Random;

public class ApplicationEntityFactory implements EntityFactory<Integer> {
    private static final Random random = new Random();
    private static final ApplicationEntityFactory factory = new ApplicationEntityFactory();

    private ApplicationEntityFactory() {
    }

    public static ApplicationEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer applicationId,
            EntityFactory.IdGenerateMode idGenerateMode) {

        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (applicationId == null) {
            applicationId = factory.getId(applicationId, idGenerateMode);
        }

        ApplicationEntity application = new ApplicationEntity();
        application.setId(applicationId);
        application.setName(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "name",
                        methodName,
                        application.getId()));
        application.setDescription(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "description",
                        methodName,
                        application.getId()));
        application.setOwner(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "owner",
                        methodName,
                        application.getId()));

        return application;
    }

    @Override
    public Integer getIdFromJdbc() {
        return JdbcApplicationEntityIdProvider.generateId();
    }

    @Override
    public Integer generateRandomId() {
        return random.nextInt();
    }
}
