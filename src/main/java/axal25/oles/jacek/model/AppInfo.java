package axal25.oles.jacek.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppInfo {
    private final String address;
    private final String hostname;
    private final Integer port;
}
