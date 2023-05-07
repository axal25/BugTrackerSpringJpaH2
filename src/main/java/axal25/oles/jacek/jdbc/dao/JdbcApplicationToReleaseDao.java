package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JdbcApplicationToReleaseDao {
    private JdbcApplicationToReleaseDao() {
    }

    public static Map<Integer, Integer> selectApplicationIdToReleaseIdMap(Connection connection) throws SQLException {
        String select = "SELECT * FROM " + Constants.Tables.APPLICATIONS_TO_RELEASES;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        Map<Integer, Integer> applicationIdToReleaseIdMap = new HashMap<>();
        while (resultSet.next()) {
            Integer applicationId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.APPLICATION_ID);
            Integer releaseId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.RELEASE_ID);
            applicationIdToReleaseIdMap.put(applicationId, releaseId);
        }
        return applicationIdToReleaseIdMap;
    }

    public static Optional<SimpleEntry<Integer, Integer>> selectApplicationIdToReleaseIdByApplicationId(Integer applicationId, Connection connection) throws SQLException {
        if (applicationId == null) {
            throw new SQLException(ReleaseEntity.class.getSimpleName() +
                    "'s " +
                    Constants.Tables.ApplicationsToReleases.APPLICATION_ID +
                    " cannot be null.");
        }

        String select = "SELECT * FROM " + Constants.Tables.APPLICATIONS_TO_RELEASES + " WHERE " + Constants.Tables.ApplicationsToReleases.APPLICATION_ID + " = ?1";
        PreparedStatement preparedStatement = connection.prepareStatement(select);
        preparedStatement.setInt(1, applicationId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Integer fetchedApplicationId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.APPLICATION_ID);
            Integer fetchedReleaseId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.RELEASE_ID);

            return Optional.of(new SimpleEntry<>(fetchedApplicationId, fetchedReleaseId));
        }

        return Optional.empty();
    }

    public static Optional<SimpleEntry<Integer, Integer>> selectApplicationIdToReleaseIdByReleaseId(Integer releaseId, Connection connection) throws SQLException {
        if (releaseId == null) {
            throw new SQLException(ReleaseEntity.class.getSimpleName() +
                    "'s " +
                    Constants.Tables.ApplicationsToReleases.RELEASE_ID +
                    " cannot be null.");
        }

        String select = "SELECT * FROM " + Constants.Tables.APPLICATIONS_TO_RELEASES + " WHERE " + Constants.Tables.ApplicationsToReleases.RELEASE_ID + " = ?1";
        PreparedStatement preparedStatement = connection.prepareStatement(select);
        preparedStatement.setInt(1, releaseId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Integer fetchedApplicationId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.APPLICATION_ID);
            Integer fetchedReleaseId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.RELEASE_ID);

            return Optional.of(new SimpleEntry<>(fetchedApplicationId, fetchedReleaseId));
        }

        return Optional.empty();
    }

    public static Map<Integer, Integer> selectApplicationIdToReleaseIdMapByApplicationIds(List<Integer> applicationIds, Connection connection) throws SQLException {
        if (applicationIds == null || applicationIds.stream().anyMatch(Objects::isNull)) {
            throw new SQLException(ApplicationEntity.class.getSimpleName() + "'s ids cannot be null");
        }

        if (applicationIds.isEmpty()) {
            return Map.of();
        }

        List<Integer> parameterIndexes = IntStream.range(1, applicationIds.size() + 1).boxed().collect(Collectors.toList());

        String select = String.format(
                "SELECT * FROM " + Constants.Tables.APPLICATIONS_TO_RELEASES +
                        " WHERE " + Constants.Tables.ApplicationsToReleases.APPLICATION_ID +
                        " IN (%s)",
                parameterIndexes.stream().map(i -> "?" + i).collect(Collectors.joining(", ")));

        PreparedStatement preparedStatement = connection.prepareStatement(select);

        for (Integer parameterIndex : parameterIndexes) {
            preparedStatement.setInt(parameterIndex, applicationIds.get(parameterIndex - 1));
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        Map<Integer, Integer> applicationIdToReleaseIdMap = new HashMap<>();
        while (resultSet.next()) {
            Integer applicationId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.APPLICATION_ID);
            Integer releaseId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.RELEASE_ID);
            applicationIdToReleaseIdMap.put(applicationId, releaseId);
        }
        return applicationIdToReleaseIdMap;
    }

    public static Map<Integer, Integer> selectApplicationIdToReleaseIdMapByReleaseIds(List<Integer> releaseIds, Connection connection) throws SQLException {
        if (releaseIds == null || releaseIds.stream().anyMatch(Objects::isNull)) {
            throw new SQLException(ReleaseEntity.class.getSimpleName() + "'s ids cannot be null");
        }

        if (releaseIds.isEmpty()) {
            return Map.of();
        }

        List<Integer> parameterIndexes = IntStream.range(1, releaseIds.size() + 1).boxed().collect(Collectors.toList());

        String select = String.format(
                "SELECT * FROM " + Constants.Tables.APPLICATIONS_TO_RELEASES +
                        " WHERE " + Constants.Tables.ApplicationsToReleases.RELEASE_ID +
                        " IN (%s)",
                parameterIndexes.stream().map(i -> "?" + i).collect(Collectors.joining(", ")));

        PreparedStatement preparedStatement = connection.prepareStatement(select);

        for (Integer parameterIndex : parameterIndexes) {
            preparedStatement.setInt(parameterIndex, releaseIds.get(parameterIndex - 1));
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        Map<Integer, Integer> applicationIdToReleaseIdMap = new HashMap<>();
        while (resultSet.next()) {
            Integer applicationId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.APPLICATION_ID);
            Integer releaseId = resultSet.getInt(Constants.Tables.ApplicationsToReleases.RELEASE_ID);
            applicationIdToReleaseIdMap.put(applicationId, releaseId);
        }
        return applicationIdToReleaseIdMap;
    }

    public static Optional<SimpleEntry<Integer, Integer>> insertApplicationIdToReleaseId(Integer applicationId, Integer releaseId, Connection connection) throws SQLException {
        String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                Constants.Tables.APPLICATIONS_TO_RELEASES,
                String.format("%s, %s",
                        Constants.Tables.ApplicationsToReleases.APPLICATION_ID,
                        Constants.Tables.ApplicationsToReleases.RELEASE_ID),
                "?1, ?2");

        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, applicationId);
        preparedStatement.setInt(2, releaseId);

        if (preparedStatement.executeUpdate() != 1) {
            return Optional.empty();
        }

        return Optional.of(new SimpleEntry<>(applicationId, releaseId));
    }
}
