package axal25.oles.jacek.constant;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static class EndpointPaths {

        public static final String BUG_TRACKER_CONTROLLER = "/bug-tracker";
        public static final String APPLICATION_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/application";
        public static final String RELEASE_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/release";
        public static final String TICKET_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/ticket";
    }

    public static class Tables {
        public static final String APPLICATIONS = "applications";

        public static class Applications {
            public static final String ID = "application_id";
            public static final String NAME = "app_name";
            public static final String DESCRIPTION = "description";
            public static final String OWNER = "owner";
            public static final List<String> COLUMNS = List.of(ID, NAME, DESCRIPTION, OWNER);
        }

        public static final String RELEASES = "releases";

        public static class Releases {
            public static final String ID = "id";
            public static final String RELEASE_DATE = "release_date";
            public static final String DESCRIPTION = "description";
        }

        public static final String APPLICATIONS_TO_RELEASES = "Releases_Applications";

        public static class ApplicationsToReleases {
            public static final String RELEASE_ID = "release_entity_id";
            public static final String APPLICATION_ID = "applications_application_id";
        }

        public static final String TICKETS = "tickets";

        public static class Tickets {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String STATUS = "status";
            public static final String DESCRIPTION = "description";
            public static final String APPLICATION_ID = "application_id";
        }

        public static final String RELEASES_TO_TICKETS = "ticket_release";

        public static class ReleasesToTickets {
            public static final String RELEASE_ID = "release_fk";
            public static final String TICKET_ID = "ticket_fk";
        }
    }

    public static class Database {
        public static final String databaseName = "bugtracker";
        public static final String userName = "sa";
        public static final String password = "";
    }

    public static class Formatters {
        public static final String PATTERN_DATE_TIME_FORMATTER = "yyyy-MM-dd";
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME_FORMATTER);
    }
}
