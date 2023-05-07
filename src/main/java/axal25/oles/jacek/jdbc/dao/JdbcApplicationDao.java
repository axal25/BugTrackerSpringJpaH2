package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JdbcApplicationDao {
    private JdbcApplicationDao() {
    }

    public static Optional<ApplicationEntity> insertApplication(ApplicationEntity application, Connection connection) throws SQLException {

        String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                Constants.Tables.APPLICATIONS,
                String.format("%s, %s, %s, %s",
                        Constants.Tables.Applications.ID,
                        Constants.Tables.Applications.NAME,
                        Constants.Tables.Applications.DESCRIPTION,
                        Constants.Tables.Applications.OWNER),
                "?1, ?2, ?3, ?4");

        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, application.getId());
        preparedStatement.setString(2, application.getName());
        preparedStatement.setString(3, application.getDescription());
        preparedStatement.setString(4, application.getOwner());

        if (preparedStatement.executeUpdate() == 1) {
            return Optional.of(application);
        }

        return Optional.empty();
    }

    public static List<ApplicationEntity> selectApplications(Connection connection) throws SQLException {
        String select = "SELECT * FROM " + Constants.Tables.APPLICATIONS;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        List<ApplicationEntity> applications = new ArrayList<>();
        while (resultSet.next()) {
            ApplicationEntity application = new ApplicationEntity();
            application.setId(resultSet.getInt(Constants.Tables.Applications.ID));
            application.setName(resultSet.getString(Constants.Tables.Applications.NAME));
            application.setDescription(resultSet.getString(Constants.Tables.Applications.DESCRIPTION));
            application.setOwner(resultSet.getString(Constants.Tables.Applications.OWNER));
            applications.add(application);
        }

        return applications;
    }

    public static List<Integer> selectApplicationIds(Connection connection) throws SQLException {
        String select = "SELECT " + Constants.Tables.Applications.ID + " FROM " + Constants.Tables.APPLICATIONS;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        List<Integer> applicationIds = new ArrayList<>();

        while (resultSet.next()) {
            applicationIds.add(resultSet.getInt(Constants.Tables.Applications.ID));
        }

        return applicationIds;
    }

    public static Optional<ApplicationEntity> selectApplicationById(Integer id, Connection connection) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException(ApplicationEntity.class.getSimpleName() + "'s id cannot be null.");
        }

        String select = "SELECT * FROM " + Constants.Tables.APPLICATIONS + " WHERE " + Constants.Tables.Applications.ID + " = ?1";
        PreparedStatement preparedStatement = connection.prepareStatement(select);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            ApplicationEntity application = new ApplicationEntity();
            application.setId(resultSet.getInt(Constants.Tables.Applications.ID));
            application.setName(resultSet.getString(Constants.Tables.Applications.NAME));
            application.setDescription(resultSet.getString(Constants.Tables.Applications.DESCRIPTION));
            application.setOwner(resultSet.getString(Constants.Tables.Applications.OWNER));

            return Optional.of(application);
        }

        return Optional.empty();
    }

    public static List<ApplicationEntity> selectApplicationsByIds(List<Integer> ids, Connection connection) throws SQLException {
        if (ids == null || ids.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(ApplicationEntity.class.getSimpleName() + "'s ids cannot be null");
        }

        if (ids.isEmpty()) {
            return List.of();
        }

        List<Integer> parameterIndexes = IntStream.range(1, ids.size() + 1).boxed().collect(Collectors.toList());

        String select = String.format(
                "SELECT * FROM " + Constants.Tables.APPLICATIONS +
                        " WHERE " + Constants.Tables.Applications.ID +
                        " IN (%s)",
                parameterIndexes.stream().map(i -> "?" + i).collect(Collectors.joining(", ")));
        PreparedStatement preparedStatement = connection.prepareStatement(select);

        for (Integer parameterIndex : parameterIndexes) {
            preparedStatement.setInt(parameterIndex, ids.get(parameterIndex - 1));
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        List<ApplicationEntity> applications = new ArrayList<>();
        while (resultSet.next()) {
            ApplicationEntity application = new ApplicationEntity();
            application.setId(resultSet.getInt(Constants.Tables.Applications.ID));
            application.setName(resultSet.getString(Constants.Tables.Applications.NAME));
            application.setDescription(resultSet.getString(Constants.Tables.Applications.DESCRIPTION));
            application.setOwner(resultSet.getString(Constants.Tables.Applications.OWNER));
            applications.add(application);
        }

        return applications;
    }
}
