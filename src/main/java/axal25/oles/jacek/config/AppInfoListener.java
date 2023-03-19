package axal25.oles.jacek.config;

import axal25.oles.jacek.model.AppInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class AppInfoListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private Environment environment;

    @Getter
    private AppInfo local;

    @Getter
    private AppInfo remote;

    @Getter
    private Integer port;

    public String getFullHostAddress() {
        if (remote == null) {
            throw new IllegalStateException("remote is null.");
        }
        if (remote.getHostname() == null) {
            throw new IllegalStateException("remote.hostname is null.");
        }
        if (port == null) {
            throw new IllegalStateException("port is null.");
        }
        return "http://" +
                remote.getHostname() +
                ":" +
                port;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        port = environment.getProperty("server.port", Integer.class);
        Integer server_port = environment.getProperty("server_port", Integer.class);
        local = new AppInfo(
                getLocalHost().getHostAddress(),
                getLocalHost().getHostName(),
                environment.getProperty("local.server.port", Integer.class));
        remote = new AppInfo(
                InetAddress.getLoopbackAddress().getHostAddress(),
                InetAddress.getLoopbackAddress().getHostName(),
                environment.getProperty("remote.server.port", Integer.class));
    }

    private static InetAddress getLocalHost() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
