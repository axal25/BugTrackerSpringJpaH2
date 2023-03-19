package axal25.oles.jacek.port;

import axal25.oles.jacek.config.AppInfoListener;
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
    private AppInfoListener appInfoListener;

    @Test
    void isPortNull() {
        // if the server.port=VALUE is missing from application.properties
        assertThat(appInfoListener.getPort()).isNull();
        assertThat(appInfoListener.getLocal().getPort()).isNull();
        assertThat(appInfoListener.getRemote().getPort()).isNull();
    }
}
