package axal25.oles.jacek.context.info;

import axal25.oles.jacek.model.AppInfo;
import axal25.oles.jacek.model.BugTrackerAppStatus;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequestScope
public class BugTrackerAppStatusProducer {
    private final Environment environment;

    @Getter
    private final BugTrackerAppStatus bugTrackerAppStatus;
    @Getter
    private final String fullHostAddress;
    @Getter
    private final Map<String, Optional<?>> descriptionsToInfo;

    @Autowired
    public BugTrackerAppStatusProducer(Environment environment) {
        this.environment = environment;
        AppInfo local = getLocalAppInfo();
        AppInfo remote = getRemoteAppInfo();
        Integer serverDotPort = environment.getProperty("server.port", Integer.class);
        Integer serverUnderscorePort = environment.getProperty("server_port", Integer.class);
        BugTrackerAppStatus.BugTrackerAppStatusBuilder builder = BugTrackerAppStatus.builder()
                .isAlive(true)
                .local(local)
                .remote(remote)
                .serverDotPort(serverDotPort)
                .serverUnderscorePort(serverUnderscorePort);
        Boolean isHealthy = remote.getHostAddress() != null && remote.getHostName() != null && remote.getPort() != null
                && local.getHostAddress() != null && local.getHostName() != null && local.getPort() != null;
        Optional<String> optionalFullHostAddress = remote.getFullHostAddress().isPresent() ? remote.getFullHostAddress()
                : local.getFullHostAddress().isPresent() ? local.getFullHostAddress()
                : Optional.empty();
        this.bugTrackerAppStatus = builder.isHealthy(isHealthy).build();
        this.fullHostAddress = optionalFullHostAddress.orElse(null);
        this.descriptionsToInfo = Map.of("Local " + AppInfo.class.getSimpleName(), Optional.ofNullable(local),
                "Remote " + AppInfo.class.getSimpleName(), Optional.ofNullable(remote),
                "server.port", Optional.ofNullable(serverDotPort),
                "server_port", Optional.ofNullable(serverUnderscorePort),
                "Full host address", optionalFullHostAddress);
    }

    private AppInfo getLocalAppInfo() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return new AppInfo(
                localHost.getHostAddress(),
                localHost.getHostName(),
                environment.getProperty("local.server.port", Integer.class));
    }

    private AppInfo getRemoteAppInfo() {
        InetAddress remoteHost = InetAddress.getLoopbackAddress();
        return new AppInfo(
                remoteHost.getHostAddress(),
                remoteHost.getHostName(),
                environment.getProperty("remote.server.port", Integer.class));
    }

    public Stream<String> toStateStringStream() {
        return getDescriptionsToInfo()
                .entrySet().stream().map(descriptionToOptionalInfo ->
                        new AbstractMap.SimpleEntry<>(
                                descriptionToOptionalInfo.getKey(),
                                descriptionToOptionalInfo.getValue()
                                        .orElse(null)))
                .map(descriptionToOptionalInfo ->
                        String.format("[%s] %s",
                                descriptionToOptionalInfo.getKey(),
                                descriptionToOptionalInfo.getValue()));
    }
}
