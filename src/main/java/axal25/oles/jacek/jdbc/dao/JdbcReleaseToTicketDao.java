package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JdbcReleaseToTicketDao {
    private JdbcReleaseToTicketDao() {
    }

    static Optional<AbstractMap.SimpleEntry<Integer, Integer>> insertReleaseIdToTicketId(
            Integer releaseId,
            Integer ticketId,
            Connection connection) throws SQLException {
        String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                Constants.Tables.RELEASES_TO_TICKETS,
                String.format("%s, %s",
                        Constants.Tables.ReleasesToTickets.RELEASE_ID,
                        Constants.Tables.ReleasesToTickets.TICKET_ID),
                "?1, ?2");

        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, releaseId);
        preparedStatement.setInt(2, ticketId);

        if (preparedStatement.executeUpdate() != 1) {
            return Optional.empty();
        }

        return Optional.of(new AbstractMap.SimpleEntry<>(releaseId, ticketId));
    }

    static Map<Integer, Integer> selectReleaseIdToTicketIdMap(Connection connection) throws SQLException {
        Map<Integer, Integer> releaseIdToTicketIdMap = new HashMap<>();
        String select = "SELECT * FROM " + Constants.Tables.RELEASES_TO_TICKETS;
        ResultSet resultSet = connection.createStatement().executeQuery(select);
        while (resultSet.next()) {
            Integer releaseId = resultSet.getInt(Constants.Tables.ReleasesToTickets.RELEASE_ID);
            Integer ticketId = resultSet.getInt(Constants.Tables.ReleasesToTickets.TICKET_ID);
            releaseIdToTicketIdMap.put(releaseId, ticketId);
        }
        return releaseIdToTicketIdMap;
    }
}
