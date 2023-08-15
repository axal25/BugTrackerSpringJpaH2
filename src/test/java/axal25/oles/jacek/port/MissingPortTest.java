package axal25.oles.jacek.port;

import axal25.oles.jacek.context.info.BugTrackerAppStatusProducerOnApplicationReady;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MissingPortTest {

    @Autowired
    private BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady;

    @Test
    void isPortNull() {
        // if the server.port=VALUE is missing from application.properties
        BugTrackerAppStatus bugTrackerAppStatus =
                bugTrackerAppStatusProducerOnApplicationReady.getBugTrackerAppStatusProducer().getBugTrackerAppStatus();
        assertThat(bugTrackerAppStatus.getServerDotPort()).isNull();
        assertThat(bugTrackerAppStatus.getLocal().getPort()).isNull();
        assertThat(bugTrackerAppStatus.getRemote().getPort()).isNull();
    }
}
