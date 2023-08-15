package axal25.oles.jacek.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ServletConfig {

    @Bean
    ServletRegistrationBean<WebServlet> provideH2ServletRegistrationBean() {
        ServletRegistrationBean<WebServlet> h2ServletRegistrationBean = new ServletRegistrationBean<>(new WebServlet());
        h2ServletRegistrationBean.addUrlMappings("/console/*");
        return h2ServletRegistrationBean;
    }
}
