package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;
import axal25.oles.jacek.json.JsonObject;
import axal25.oles.jacek.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcTicketDao {
    private JdbcTicketDao() {
    }

    public static Optional<TicketEntity> insertTicket(TicketEntity ticket, Connection connection) throws SQLException {
        insertApplication(ticket, connection);

        String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                Constants.Tables.TICKETS,
                String.format("%s, %s, %s, %s, %s",
                        Constants.Tables.Tickets.ID,
                        Constants.Tables.Tickets.TITLE,
                        Constants.Tables.Tickets.STATUS,
                        Constants.Tables.Tickets.DESCRIPTION,
                        Constants.Tables.Tickets.APPLICATION_ID),
                "?1, ?2, ?3, ?4, ?5");

        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, ticket.getId());
        preparedStatement.setString(2, ticket.getTitle());
        preparedStatement.setString(3, ticket.getStatus());
        preparedStatement.setString(4, ticket.getDescription());
        preparedStatement.setInt(5, ticket.getApplication().getId());

        if (preparedStatement.executeUpdate() != 1) {
            return Optional.empty();
        }

        insertRelease(ticket, connection);
        insertReleaseIdToTicketId(ticket, connection);

        return Optional.of(ticket);
    }

    private static void insertApplication(TicketEntity ticket, Connection connection) throws SQLException {
        if (ticket.getApplication() == null) {
            return;
        }
        Optional<ApplicationEntity> selected =
                JdbcApplicationDao.selectApplicationById(ticket.getApplication().getId(), connection);
        if (selected.isPresent()) {
            if (!ticket.getApplication().equals(selected.get())) {
                throw new JdbcDaoRuntimeException(ApplicationEntity.class.getSimpleName() +
                        " already exist with id: " +
                        ticket.getApplication().getId() +
                        "." +
                        "\r\n" +
                        "Already existing " +
                        ApplicationEntity.class.getSimpleName() +
                        ": " +
                        selected.get().toJsonPrettyString() +
                        "." +
                        "\r\n" +
                        "Attempted to be inserted " +
                        ApplicationEntity.class.getSimpleName() +
                        ": " +
                        ticket.getApplication().toJsonPrettyString() +
                        ".");
            }
            return;
        }
        Optional<ApplicationEntity> optionalApp = JdbcApplicationDao.insertApplication(ticket.getApplication(), connection);
        if (optionalApp.isEmpty()
                || !ticket.getApplication().equals(optionalApp.get())) {
            throw new JdbcDaoRuntimeException("Couldn't insert " +
                    ApplicationEntity.class.getSimpleName() +
                    ": " +
                    ticket.getApplication().toJsonPrettyString() +
                    "." +
                    "\r\n" +
                    "Returned " +
                    ApplicationEntity.class.getSimpleName() +
                    ": " +
                    (optionalApp.isPresent()
                            ? optionalApp.get().toJsonPrettyString()
                            : Optional.empty()) +
                    ".");
        }
    }

    private static void insertRelease(TicketEntity ticket, Connection connection) throws SQLException {
        if (ticket.getRelease() == null) {
            return;
        }
        Optional<ReleaseEntity> optionalRelease = JdbcReleaseDao.insertRelease(ticket.getRelease(), connection);
        if (optionalRelease.isEmpty()
                || !ticket.getRelease().equals(optionalRelease.get())) {
            throw new JdbcDaoRuntimeException("Couldn't insert " +
                    ReleaseEntity.class.getSimpleName() +
                    ": " +
                    ticket.getRelease().toJsonPrettyString() +
                    "." +
                    "\r\n" +
                    "Returned " +
                    ReleaseEntity.class.getSimpleName() +
                    ": " +
                    (optionalRelease.isPresent()
                            ? optionalRelease.get().toJsonPrettyString()
                            : Optional.empty()) +
                    ".");
        }
    }

    private static void insertReleaseIdToTicketId(TicketEntity ticket, Connection connection) throws SQLException {
        if (ticket.getRelease() == null) {
            return;
        }
        Optional<AbstractMap.SimpleEntry<Integer, Integer>> optionalReleaseIdToTicketId =
                JdbcReleaseToTicketDao.insertReleaseIdToTicketId(ticket.getRelease().getId(), ticket.getId(), connection);
        if (optionalReleaseIdToTicketId.isEmpty()
                || !ticket.getRelease().getId().equals(optionalReleaseIdToTicketId.get().getKey())
                || !ticket.getId().equals(optionalReleaseIdToTicketId.get().getValue())) {
            throw new JdbcDaoRuntimeException("Couldn't insert into " +
                    Constants.Tables.RELEASES_TO_TICKETS +
                    " " +
                    Constants.Tables.ReleasesToTickets.RELEASE_ID
                    + "-to-" +
                    Constants.Tables.ReleasesToTickets.TICKET_ID +
                    ": " +
                    new AbstractMap.SimpleEntry<>(ticket.getRelease().getId(), ticket.getId()) +
                    "." +
                    "\r\n" +
                    "Returned " +
                    Constants.Tables.ReleasesToTickets.RELEASE_ID
                    + "-to-" +
                    Constants.Tables.ReleasesToTickets.TICKET_ID +
                    ": " +
                    (optionalReleaseIdToTicketId.isPresent()
                            ? optionalReleaseIdToTicketId.get()
                            : Optional.empty()) +
                    ".");
        }
    }

    public static List<Integer> selectTicketIds(Connection connection) throws SQLException {
        String select = "SELECT " + Constants.Tables.Tickets.ID + " FROM " + Constants.Tables.TICKETS;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        List<Integer> ticketIds = new ArrayList<>();

        while (resultSet.next()) {
            ticketIds.add(resultSet.getInt(Constants.Tables.Tickets.ID));
        }

        return ticketIds;
    }

    public static List<TicketEntity> selectTickets(Connection connection) throws SQLException {
        List<ApplicationEntity> applications = JdbcApplicationDao.selectApplications(connection);
        List<ReleaseEntity> releases = JdbcReleaseDao.selectReleases(connection);
        Map<Integer, Integer> releaseIdToTicketIds = JdbcReleaseToTicketDao.selectReleaseIdToTicketIdMap(connection);

        String select = "SELECT * FROM " + Constants.Tables.TICKETS;
        ResultSet resultSet = connection.createStatement().executeQuery(select);
        List<TicketEntity> tickets = new ArrayList<>();
        while (resultSet.next()) {
            TicketEntity ticket = new TicketEntity();
            ticket.setId(resultSet.getInt(Constants.Tables.Tickets.ID));
            ticket.setTitle(resultSet.getString(Constants.Tables.Tickets.TITLE));
            ticket.setStatus(resultSet.getString(Constants.Tables.Tickets.STATUS));
            ticket.setDescription(resultSet.getString(Constants.Tables.Tickets.DESCRIPTION));

            Integer applicationId = resultSet.getInt(Constants.Tables.Tickets.APPLICATION_ID);

            ticket.setApplication(filterMatchingApp(ticket, applicationId, applications));
            ticket.setRelease(filterMatchingRelease(ticket, releases, releaseIdToTicketIds));

            tickets.add(ticket);
        }
        return tickets;
    }

    private static ApplicationEntity filterMatchingApp(TicketEntity ticket, Integer applicationId, List<ApplicationEntity> applications) {
        return applications.stream()
                .filter(app -> applicationId.equals(app.getId())).findAny()
                .orElseThrow(() -> new JdbcDaoRuntimeException("Could not find " +
                        ApplicationEntity.class.getSimpleName() +
                        " having " +
                        Constants.Tables.Applications.ID +
                        " matching " +
                        TicketEntity.class.getSimpleName() +
                        "'s " +
                        Constants.Tables.Tickets.APPLICATION_ID +
                        " = " +
                        applicationId +
                        "." +
                        "\r\n" +
                        "Failed to fetch " +
                        TicketEntity.class.getSimpleName() +
                        ": " +
                        ticket.toJsonPrettyString() +
                        "." +
                        "\r\n" +
                        "Found " +
                        ApplicationEntity.class.getSimpleName() +
                        "s: " +
                        CollectionUtils.lengthyElementsToString(applications.stream()
                                .map(JsonObject::toJsonPrettyString)
                                .collect(Collectors.toList()))));
    }

    private static ReleaseEntity filterMatchingRelease(TicketEntity ticket, List<ReleaseEntity> releases, Map<Integer, Integer> releaseIdToTicketIds) {
        Integer releaseId = releaseIdToTicketIds.entrySet().stream()
                .filter(releaseIdToTicketId -> ticket.getId()
                        .equals(releaseIdToTicketId.getValue()))
                .map(Map.Entry::getKey)
                .findAny()
                .orElseThrow(() -> new JdbcDaoRuntimeException("Could not find " +
                        Constants.Tables.ReleasesToTickets.RELEASE_ID +
                        " from " +
                        Constants.Tables.RELEASES_TO_TICKETS +
                        " having " +
                        Constants.Tables.ReleasesToTickets.TICKET_ID +
                        " matching " +
                        TicketEntity.class.getSimpleName() +
                        "'s " +
                        Constants.Tables.Tickets.ID +
                        " = " +
                        ticket.getId() +
                        "." +
                        "\r\n" +
                        "Failed to fetch " +
                        TicketEntity.class.getSimpleName() +
                        ": " +
                        ticket.toJsonPrettyString() +
                        "." +
                        "\r\n" +
                        "Found " +
                        Constants.Tables.RELEASES_TO_TICKETS +
                        ": " +
                        releaseIdToTicketIds +
                        "."));
        return releases.stream()
                .filter(release -> releaseId.equals(release.getId()))
                .findAny()
                .orElseThrow(() -> new JdbcDaoRuntimeException("Could not find " +
                        ReleaseEntity.class.getSimpleName() +
                        " having " +
                        Constants.Tables.Releases.ID +
                        " matching "
                        + Constants.Tables.RELEASES_TO_TICKETS +
                        "'s " +
                        Constants.Tables.ReleasesToTickets.RELEASE_ID +
                        ".\r\nFound " +
                        ReleaseEntity.class.getSimpleName() +
                        "s: " +
                        CollectionUtils.lengthyElementsToString(releases.stream()
                                .map(JsonObject::toJsonPrettyString)
                                .collect(Collectors.toList()))));
    }
}
