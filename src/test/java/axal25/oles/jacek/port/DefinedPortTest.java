package axal25.oles.jacek.port;

import axal25.oles.jacek.context.info.BugTrackerAppStatusProducerOnApplicationReady;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8090")
public class DefinedPortTest {

    @Autowired
    private BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady;

    @Test
    void isPortNull() {
        BugTrackerAppStatus bugTrackerAppStatus =
                bugTrackerAppStatusProducerOnApplicationReady.getBugTrackerAppStatusProducer().getBugTrackerAppStatus();
        assertThat(bugTrackerAppStatus.getServerDotPort()).isEqualTo(8090);
        assertThat(bugTrackerAppStatus.getLocal().getPort()).isEqualTo(8090);
        assertThat(bugTrackerAppStatus.getRemote().getPort()).isNull();
    }
}
