package axal25.oles.jacek.port;

import axal25.oles.jacek.config.AppInfoListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8090")
public class DefinedPortTest {

    @Autowired
    private AppInfoListener appInfoListener;

    @Test
    void isPortNull() {
        assertThat(appInfoListener.getPort()).isEqualTo(8090);
        assertThat(appInfoListener.getLocal().getPort()).isEqualTo(8090);
        assertThat(appInfoListener.getRemote().getPort()).isNull();
    }
}
