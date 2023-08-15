package axal25.oles.jacek.constant;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static class OrderBy {
        public final static String ASC = "ASC";
        public final static String DESC = "DESC";
    }

    public static class EndpointPaths {
        public static final String H2_DB_CONSOLE = "/console";

        public static final String BUG_TRACKER_CONTROLLER = "/bug-tracker";
        public static final String APPLICATION_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/application";
        public static final String RELEASE_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/release";
        public static final String TICKET_CONTROLLER =
                BUG_TRACKER_CONTROLLER + "/ticket";

        public static class WebEndpointPaths {
            public static final String WEB_CONTROLLER = BUG_TRACKER_CONTROLLER + "/web";
            public static final String APPLICATION_WEB_CONTROLLER = WEB_CONTROLLER + "/application";
        }
    }

    public static class Templates {
        public static final String ALL_APPLICATIONS = "all_applications";
    }

    public static class Tables {
        public static final String APPLICATIONS = "applications";
        public static final String RELEASES = "releases";
        public static final String APPLICATIONS_TO_RELEASES = "Releases_Applications";
        public static final String TICKETS = "tickets";
        public static final String RELEASES_TO_TICKETS = "ticket_release";

        public static class Applications {
            public static final String ID = "application_id";
            public static final String NAME = "app_name";
            public static final String DESCRIPTION = "description";
            public static final String OWNER = "owner";
            public static final String ORDER_BY = Constants.Tables.Applications.ID + " " + Constants.OrderBy.ASC;
        }

        public static class Releases {
            public static final String ID = "id";
            public static final String RELEASE_DATE = "release_date";
            public static final String DESCRIPTION = "description";
        }

        public static class ApplicationsToReleases {
            public static final String RELEASE_ID = "release_entity_id";
            public static final String APPLICATION_ID = "applications_application_id";
        }

        public static class Tickets {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String STATUS = "status";
            public static final String DESCRIPTION = "description";
            public static final String APPLICATION_ID = "application_id";
        }

        public static class ReleasesToTickets {
            public static final String RELEASE_ID = "release_fk";
            public static final String TICKET_ID = "ticket_fk";
        }
    }

    public static class Database {
        public static final String databaseName = "bugtracker";
        public static final String userName = "sa";
        public static final String password = "sa";
    }

    public static class Formatters {
        public static final String PATTERN_DATE_TIME_FORMATTER = "yyyy-MM-dd";
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME_FORMATTER);
    }
}
