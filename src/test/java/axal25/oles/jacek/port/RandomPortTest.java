package axal25.oles.jacek.port;

import axal25.oles.jacek.config.AppInfoListener;
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
    private AppInfoListener appInfoListener;

    @Test
    void isPortRandom() {
        assertThat(appInfoListener.getPort()).isEqualTo(0);
        assertThat(appInfoListener.getLocal().getPort()).isNotNull();
        assertThat(appInfoListener.getLocal().getPort() > -1).isTrue();
        assertThat(appInfoListener.getRemote().getPort()).isNull();
    }
}
