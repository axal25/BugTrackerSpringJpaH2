package axal25.oles.jacek.port;

import axal25.oles.jacek.context.info.BugTrackerAppStatusProducerOnApplicationReady;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "server.port=0")
@ExtendWith(SpringExtension.class)
public class RandomPortTest {

    @Autowired
    private BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady;

    @Test
    void isPortRandom() {
        BugTrackerAppStatus bugTrackerAppStatus =
                bugTrackerAppStatusProducerOnApplicationReady.getBugTrackerAppStatusProducer().getBugTrackerAppStatus();
        assertThat(bugTrackerAppStatus.getServerDotPort()).isEqualTo(0);
        assertThat(bugTrackerAppStatus.getLocal().getPort()).isNotNull();
        assertThat(bugTrackerAppStatus.getLocal().getPort() > -1).isTrue();
        assertThat(bugTrackerAppStatus.getRemote().getPort()).isNull();
    }
}
