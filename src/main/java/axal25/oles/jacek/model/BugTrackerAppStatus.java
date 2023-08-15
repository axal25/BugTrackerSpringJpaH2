package axal25.oles.jacek.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class BugTrackerAppStatus {
    private final Boolean isAlive;
    private final Boolean isHealthy;
    private final AppInfo local;
    private final AppInfo remote;
    private final Integer serverDotPort;
    private final Integer serverUnderscorePort;
}
