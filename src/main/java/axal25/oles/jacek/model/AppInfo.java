package axal25.oles.jacek.model;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Getter
@ToString
public class AppInfo {
    private static final String protocol = "http";
    private final String hostAddress;
    private final String hostName;
    private final Integer port;
    private final Optional<String> fullHostAddress;

    public AppInfo(String hostAddress, String hostName, Integer port) {
        this.hostAddress = hostAddress;
        this.hostName = hostName;
        this.port = port;
        this.fullHostAddress = getEvaluatedFullHostAddress();
    }

    private Optional<String> getEvaluatedFullHostAddress() {
        Optional<String> hostAddressPrefix = StringUtils.isBlank(hostAddress)
                ? Optional.empty() : Optional.of(String.format("%s://%s", protocol, hostAddress));
        Optional<String> hostNamePrefix = StringUtils.isBlank(hostName)
                ? Optional.empty() : Optional.of(String.format("%s://%s", protocol, hostName));
        Optional<String> prefix = hostAddressPrefix.isPresent()
                ? hostAddressPrefix : hostNamePrefix;
        Optional<String> portSuffix = port == null
                ? Optional.empty() : Optional.of(String.format(":%d", port));

        return prefix.isEmpty() || portSuffix.isEmpty()
                ? Optional.empty()
                : Optional.of(String.format("%s%s", prefix.get(), portSuffix.get()));
    }
}
