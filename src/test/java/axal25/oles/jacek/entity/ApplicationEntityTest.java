package axal25.oles.jacek.entity;

import axal25.oles.jacek.entity.factory.ApplicationEntityFactory;
import axal25.oles.jacek.entity.factory.EntityFactory;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ApplicationEntityTest {

    public static ApplicationEntity getDeepCopy(ApplicationEntity inputApplication) {
        ApplicationEntity deepCopyApplication = new ApplicationEntity();

        deepCopyApplication.setId(inputApplication.getId());
        deepCopyApplication.setName(inputApplication.getName());
        deepCopyApplication.setDescription(inputApplication.getDescription());
        deepCopyApplication.setOwner(inputApplication.getOwner());

        return deepCopyApplication;
    }

    @Test
    public void deepCopy_isEqualToExpected() {
        ApplicationEntity inputApp = ApplicationEntityFactory.produce(
                "deepCopy_isEqualToExpected",
                getClass(),
                null,
                EntityFactory.IdGenerateMode.RANDOM);

        assertThat(inputApp.deepCopy()).isEqualTo(getDeepCopy(inputApp));
    }
}
