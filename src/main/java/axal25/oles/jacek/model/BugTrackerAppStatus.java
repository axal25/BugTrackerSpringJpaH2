package axal25.oles.jacek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BugTrackerAppStatus {
    private final Boolean isAlive;
    private final Boolean isHealthy;
    private final AppInfo local;
    private final AppInfo remote;
    private final Integer port;
    private final String fullHostAddress;
}
