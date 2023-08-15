package axal25.oles.jacek.config;

import org.h2.server.web.WebServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.google.common.truth.Truth.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2ServletConfig.class})
public class H2ServletConfigTest {

    @Autowired
    private ServletRegistrationBean<WebServlet> h2ServletRegistrationBean;

    @Test
    void providedH2ServletRegistrationBean() {
        assertThat(h2ServletRegistrationBean).isNotNull();
    }
}
