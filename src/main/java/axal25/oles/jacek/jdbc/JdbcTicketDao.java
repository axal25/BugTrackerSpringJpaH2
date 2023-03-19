package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcTicketDao {
    private final Connection connection = DatabaseUtils.getConnection();
    private final JdbcApplicationDao appDao = new JdbcApplicationDao();
    private final JdbcReleaseDao releaseDao = new JdbcReleaseDao();
    private final JdbcReleaseToTicketDao releaseToTicketDao = new JdbcReleaseToTicketDao();

    public JdbcTicketDao() throws SQLException {
    }

    public Optional<TicketEntity> insertTicket(TicketEntity ticket) throws SQLException {
        Optional<ApplicationEntity> optionalApp = appDao.insertApplication(ticket.getApplication());
        if (optionalApp.isEmpty() || !ticket.getApplication().equals(optionalApp.get())) {
            return Optional.empty();
        }

        if (ticket.getRelease() != null) {
            Optional<ReleaseEntity> optionalRelease = releaseDao.insertRelease(ticket.getRelease());
            if (optionalRelease.isEmpty() || !ticket.getRelease().equals(optionalRelease.get())) {
                return Optional.empty();
            }
        }

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

        if (ticket.getRelease() != null) {
            Optional<AbstractMap.SimpleEntry<Integer, Integer>> optionalReleaseIdToTicketId =
                    releaseToTicketDao.insertReleaseIdToTicketId(ticket.getRelease().getId(), ticket.getId());
            if (optionalReleaseIdToTicketId.isEmpty()
                    || !ticket.getRelease().getId().equals(optionalReleaseIdToTicketId.get().getKey())
                    || !ticket.getId().equals(optionalReleaseIdToTicketId.get().getValue())) {
                return Optional.empty();
            }
        }

        return Optional.of(ticket);
    }

    public List<TicketEntity> selectTickets() throws SQLException {
        List<ApplicationEntity> applications = appDao.selectApplications();
        List<ReleaseEntity> releases = releaseDao.selectReleases();
        Map<Integer, Integer> releaseIdToTicketIds = releaseToTicketDao.selectReleaseIdToTicketIdMap();

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
            ApplicationEntity application = applications.stream()
                    .filter(app -> applicationId.equals(app.getId())).findAny()
                    .orElseThrow(() -> new SQLException("Could not find " +
                            ApplicationEntity.class.getSimpleName() +
                            " having " +
                            Constants.Tables.Applications.ID +
                            " matching " +
                            TicketEntity.class.getSimpleName() +
                            "'s " +
                            Constants.Tables.Tickets.APPLICATION_ID +
                            " = " +
                            applicationId +
                            "."));
            ticket.setApplication(application);

            Integer releaseId = releaseIdToTicketIds.entrySet().stream()
                    .filter(releaseIdToTicketId -> ticket.getId()
                            .equals(releaseIdToTicketId.getValue()))
                    .map(Map.Entry::getKey)
                    .findAny()
                    .orElseThrow(() -> new SQLException("Couldn't not find " +
                            Constants.Tables.ReleasesToTickets.RELEASE_ID +
                            " from " +
                            Constants.Tables.RELEASES_TO_TICKETS +
                            " having " +
                            Constants.Tables.ReleasesToTickets.TICKET_ID +
                            " matching" +
                            TicketEntity.class.getSimpleName() +
                            "'s " +
                            Constants.Tables.Tickets.ID +
                            " = " +
                            ticket.getId()));
            ReleaseEntity release = releases.stream()
                    .filter(oneOfReleases -> releaseId.equals(oneOfReleases.getId()))
                    .findAny().orElseThrow(() -> new SQLException("Couldn't find " +
                            ReleaseEntity.class.getSimpleName() +
                            " having " +
                            Constants.Tables.Releases.ID +
                            " matching "
                            + Constants.Tables.RELEASES_TO_TICKETS +
                            "'s " +
                            Constants.Tables.ReleasesToTickets.RELEASE_ID +
                            "."));
            ticket.setRelease(release);

            tickets.add(ticket);
        }
        return tickets;
    }
}
