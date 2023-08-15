package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.factory.EntityFactory;
import axal25.oles.jacek.entity.factory.ReleaseEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class JdbcReleaseTransactionalDaoTest {
    private static final Random random = new Random();

    @Autowired
    private JdbcReleaseTransactionalDao jdbcReleaseTransactionalDao;

    @Test
    public void selectReleases_areSelectedSuccessfully() {
        List<ReleaseEntity> inputReleases = IntStream.range(0, random.nextInt(10))
                .mapToObj(i -> ReleaseEntityFactory.produce(
                        "selectReleases_areSelectedSuccessfully",
                        getClass(),
                        null,
                        null,
                        EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER))
                .collect(Collectors.toList());

        inputReleases.forEach(this::insertReleaseTransactional);

        List<ReleaseEntity> selecteds = jdbcReleaseTransactionalDao.selectReleasesTransactional();

        assertThat(selecteds).containsAtLeastElementsIn(inputReleases);
    }

    @Test
    public void insertReleaseTransactional_isInsertedSuccessfully() {
        ReleaseEntity inputRelease = ReleaseEntityFactory.produce(
                "insertReleaseTransactional_isInsertedSuccessfully",
                getClass(),
                null,
                null,
                EntityFactory.IdGenerateMode.FROM_ENTITY_MANAGER);

        insertReleaseTransactional(inputRelease);
    }

    private void insertReleaseTransactional(ReleaseEntity inputRelease) {
        ReleaseEntity inserted =
                jdbcReleaseTransactionalDao.insertReleaseTransactional(inputRelease);

        assertThat(inserted).isEqualTo(inputRelease);
    }
}
