package axal25.oles.jacek.dao.mock;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.jdbc.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Prefiller {
    private static final int idOffset = 100;
    private static final int entityAmount = 10;

    public static void prefillInMemDb() {
        Optional<Connection> optionalConnection;
        try {
            Connection connection = DatabaseUtils.getConnection();
            optionalConnection = Optional.of(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // TODO: use JdbcUtils
        optionalConnection.ifPresent(connection -> {
            List<Integer> newApplicationIds = prefillApplications(connection);
            List<Integer> newReleasesIds = prefillReleases(connection);
            List<Integer> newTicketIds = prefillTickets(connection);
        });
    }

    private static List<Integer> prefillApplications(Connection connection) {
        return IntStream.range(idOffset, idOffset + entityAmount).mapToObj(id -> {
            String insert = String.format(
                    "INSERT INTO %s (%s) VALUES(%s)",
                    Constants.Tables.APPLICATIONS,
                    "application_id, app_name, description, owner",
                    String.format(
                            "%d, 'application name %d prefilled', 'application description %d prefilled', 'application owner %d prefilled'",
                            id, id, id, id));
            try {
                int response = connection.createStatement().executeUpdate(insert);
                return response == 1 ? id : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    private static List<Integer> prefillReleases(Connection connection) {
        return IntStream.range(idOffset, idOffset + entityAmount).mapToObj(id -> {
            String insert = String.format(
                    "INSERT INTO %s (%s) VALUES(%s)",
                    Constants.Tables.RELEASES,
                    "id, release_date, description",
                    String.format(
                            "%d, '%s', 'release description %d prefilled'",
                            id,
                            LocalDate.now()
                                    .minusDays(id)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            id));
            try {
                int response = connection.createStatement().executeUpdate(insert);
                return response == 1 ? id : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    private static void prefillReleasesApplications(
            Connection connection,
            List<Integer> newApplicationIds,
            List<Integer> newReleasesIds) {
        List<Integer> unusedApplicationIds = List.copyOf(newApplicationIds);
        // Map<String, List<String>> releaseIdToApplicationIds =
    }

    private static List<Integer> prefillTickets(Connection connection) {
        return IntStream.range(idOffset, idOffset + entityAmount).mapToObj(id -> {
            int applicationId = id;
            String insert = String.format(
                    "INSERT INTO %s (%s) VALUES(%s)",
                    Constants.Tables.TICKETS,
                    "id, title, description, application_id, status",
                    String.format(
                            "%d, 'title %d', 'description %d', %d, 'status %d'",
                            id, id, id, applicationId, id));
            try {
                int response = connection.createStatement().executeUpdate(insert);
                return response == 1 ? id : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(toList());
    }
}
