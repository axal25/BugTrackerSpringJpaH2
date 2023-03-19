package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.jdbc.JdbcReleaseEntityIdProvider;
import axal25.oles.jacek.util.localdate.LocalDateUtils;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@AllArgsConstructor
public class ReleaseEntityFactory {
    private static final Random random = new Random();

    public static ReleaseEntity produce(
            String expectedMethodName,
            Class<?> methodOwnerClass,
            @Nullable Integer releaseId,
            @Nullable List<ApplicationEntity> applications) {
        String methodName = MethodNameValidator.getValidatedMethodName(expectedMethodName, methodOwnerClass);

        if (releaseId == null) {
            releaseId = JdbcReleaseEntityIdProvider.generateId();
        }

        if (applications == null) {
            List<ApplicationEntity> tmpApp = new ArrayList<>();
            IntStream.range(0, random.nextInt() % 10).forEach(unused ->
                    tmpApp.add(
                            ApplicationEntityFactory
                                    .produce(expectedMethodName, methodOwnerClass, null)));
            applications = tmpApp;
        }

        ReleaseEntity release = new ReleaseEntity();
        release.setId(releaseId);
        LocalDate releaseDate = LocalDateUtils.produceRandom();
        releaseDate = LocalDate.of(
                Math.abs(releaseDate.getYear() % 10000),
                releaseDate.getMonth(),
                releaseDate.getDayOfMonth());
        release.setReleaseDate(releaseDate);
        release.setDescription(FieldValueFormatter.getStringValue(
                ReleaseEntity.class,
                "name",
                methodName,
                release.getId()));
        release.setApplications(applications);

        return release;
    }
}
