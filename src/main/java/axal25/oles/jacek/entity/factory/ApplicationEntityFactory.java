package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.dao.application.DaoApplicationEntityIdProvider;
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
            @Nullable Integer id,
            EntityFactory.IdGenerateMode idGenerateMode) {

        String methodName = MethodNameValidator
                .getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (id == null) {
            id = factory.getId(id, idGenerateMode);
        }

        Integer idForNonIdFields = factory.getIdForNonIdFields(id, idGenerateMode);

        ApplicationEntity application = new ApplicationEntity();
        application.setId(id);
        application.setName(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "name",
                        methodName,
                        idForNonIdFields));
        application.setDescription(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "description",
                        methodName,
                        idForNonIdFields));
        application.setOwner(
                FieldValueFormatter.getStringValue(
                        ApplicationEntity.class,
                        "owner",
                        methodName,
                        idForNonIdFields));

        return application;
    }

    @Override
    public Integer getIdFromJdbc() {
        return JdbcApplicationEntityIdProvider.generateId();
    }

    @Override
    public Integer getIdFromEntityManager() {
        return DaoApplicationEntityIdProvider.generateId();
    }

    @Override
    public Integer generateRandomId() {
        return random.nextInt();
    }
}
