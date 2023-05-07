package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.jdbc.id.provider.JdbcReleaseEntityIdProvider;
import axal25.oles.jacek.util.LocalDateUtils;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReleaseEntityFactory implements EntityFactory<Integer> {
    private static final Random random = new Random();
    private static final ReleaseEntityFactory factory = new ReleaseEntityFactory();

    private ReleaseEntityFactory() {
    }

    public static ReleaseEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer releaseId,
            @Nullable List<ApplicationEntity> applications,
            EntityFactory.IdGenerateMode idGenerateMode) {

        String methodName = MethodNameValidator.getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (releaseId == null) {
            releaseId = factory.getId(releaseId, idGenerateMode);
        }

        if (applications == null) {
            applications = IntStream.range(0, random.nextInt() % 10)
                    .mapToObj(unused -> ApplicationEntityFactory.produce(
                            expectedMethodName,
                            methodOwnerClass,
                            null,
                            idGenerateMode))
                    .sorted(Comparator.comparingInt(ApplicationEntity::getId))
                    .collect(Collectors.toList());
        }

        ReleaseEntity release = new ReleaseEntity();
        release.setId(releaseId);
        release.setReleaseDate(LocalDateUtils.produceRandomPastSinceEpoch());
        release.setDescription(FieldValueFormatter.getStringValue(
                ReleaseEntity.class,
                "name",
                methodName,
                release.getId()));
        release.setApplications(applications);

        return release;
    }

    @Override
    public Integer getIdFromJdbc() {
        return JdbcReleaseEntityIdProvider.generateId();
    }

    @Override
    public Integer generateRandomId() {
        return random.nextInt();
    }
}
