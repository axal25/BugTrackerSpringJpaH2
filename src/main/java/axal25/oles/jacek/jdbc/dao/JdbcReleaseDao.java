package axal25.oles.jacek.jdbc.dao;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.json.JsonObject;
import axal25.oles.jacek.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcReleaseDao {
    private JdbcReleaseDao() {
    }

    public static Optional<ReleaseEntity> insertRelease(ReleaseEntity release, Connection connection) throws SQLException {
        insertApplications(release, connection);

        String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                Constants.Tables.RELEASES,
                String.format("%s, %s, %s",
                        Constants.Tables.Releases.ID,
                        Constants.Tables.Releases.RELEASE_DATE,
                        Constants.Tables.Releases.DESCRIPTION),
                "?1, ?2, ?3");

        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, release.getId());
        preparedStatement.setString(
                2,
                release.getReleaseDate() == null
                        ? null
                        : release.getReleaseDate().format(Constants.Formatters.DATE_TIME_FORMATTER));
        preparedStatement.setString(3, release.getDescription());

        if (preparedStatement.executeUpdate() != 1) {
            return Optional.empty();
        }

        insertApplicationIdToReleaseIds(release, connection);

        return Optional.of(release);
    }

    public static List<Integer> selectReleaseIds(Connection connection) throws SQLException {
        String select = "SELECT " + Constants.Tables.Releases.ID + " FROM " + Constants.Tables.RELEASES;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        List<Integer> releaseIds = new ArrayList<>();
        while (resultSet.next()) {
            releaseIds.add(resultSet.getInt(Constants.Tables.Releases.ID));
        }

        return releaseIds;
    }

    public static List<ReleaseEntity> selectReleases(Connection connection) throws SQLException {
        String select = "SELECT * FROM " + Constants.Tables.RELEASES;
        ResultSet resultSet = connection.createStatement().executeQuery(select);

        List<ReleaseEntity> releases = new ArrayList<>();
        while (resultSet.next()) {
            ReleaseEntity release = new ReleaseEntity();
            release.setId(resultSet.getInt(Constants.Tables.Releases.ID));

            String releaseDateColumn = resultSet.getString(Constants.Tables.Releases.RELEASE_DATE);
            release.setReleaseDate(
                    releaseDateColumn == null
                            ? null
                            : LocalDate.parse(releaseDateColumn, Constants.Formatters.DATE_TIME_FORMATTER));
            release.setDescription(resultSet.getString(Constants.Tables.Releases.DESCRIPTION));
            releases.add(release);
        }

        return getReleasesWithUpdatedApplications(releases, connection);
    }

    private static List<ReleaseEntity> getReleasesWithUpdatedApplications(List<ReleaseEntity> releases, Connection connection)
            throws SQLException {
        Map<Integer, Integer> applicationIdToReleaseIds =
                JdbcApplicationToReleaseDao.selectApplicationIdToReleaseIdMapByReleaseIds(
                        releases.stream()
                                .map(ReleaseEntity::getId)
                                .collect(Collectors.toList()),
                        connection);
        List<ApplicationEntity> selectedApps = JdbcApplicationDao.selectApplicationsByIds(
                List.copyOf(applicationIdToReleaseIds.keySet()),
                connection);

        releases = releases.stream()
                .peek(release -> {
                    Set<Integer> releaseAppIds = applicationIdToReleaseIds.entrySet().stream()
                            .filter(appIdToReleaseId -> release.getId()
                                    .equals(appIdToReleaseId.getValue()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
                    List<ApplicationEntity> releaseApps = selectedApps.stream()
                            .filter(application -> releaseAppIds.contains(application.getId()))
                            .collect(Collectors.toList());
                    release.setApplications(releaseApps);
                })
                .collect(Collectors.toList());

        return releases;
    }

    private static void insertApplications(ReleaseEntity release, Connection connection) throws SQLException {
        List<ApplicationEntity> existingApps = JdbcApplicationDao.selectApplicationsByIds(
                release.getApplications().stream()
                        .map(ApplicationEntity::getId)
                        .collect(Collectors.toList()),
                connection);

        existingApps.forEach(existingApp -> {
            ApplicationEntity matchingReleaseApp = release.getApplications().stream()
                    .filter(releaseApp -> existingApp.getId().equals(releaseApp.getId()))
                    .findAny()
                    .orElseThrow(() -> new JdbcDaoRuntimeException("Existing " +
                            ApplicationEntity.class.getSimpleName() +
                            " has no matching " +
                            ReleaseEntity.class.getSimpleName() +
                            "'s " + ApplicationEntity.class.getSimpleName() +
                            " by " +
                            Constants.Tables.Applications.ID +
                            "." +
                            "\r\n" +
                            ReleaseEntity.class.getSimpleName() +
                            "'s " +
                            ApplicationEntity.class.getSimpleName() +
                            "s: " +
                            CollectionUtils.lengthyStringsToString(
                                    release.getApplications().stream()
                                            .map(JsonObject::toJsonPrettyString)
                                            .collect(Collectors.toList())) +
                            "." +
                            "\r\n" +
                            "Existing " +
                            ApplicationEntity.class.getSimpleName() +
                            ": " +
                            existingApp.toJsonPrettyString() +
                            "."));
            if (!matchingReleaseApp.equals(existingApp)) {
                throw new JdbcDaoRuntimeException("Matching " +
                        ReleaseEntity.class.getSimpleName() +
                        "'s" +
                        ApplicationEntity.class.getSimpleName() +
                        ": " +
                        matchingReleaseApp +
                        "\r\n" +
                        "and" +
                        "\r\n" +
                        "Existing " +
                        ApplicationEntity.class.getSimpleName() +
                        ": " +
                        existingApp.toJsonPrettyString() +
                        "\r\n" +
                        "are not equal.");
            }
        });

        List<ApplicationEntity> toBeInsertedApps = release.getApplications().stream()
                .filter(app -> !existingApps.contains(app))
                .collect(Collectors.toList());
        for (ApplicationEntity toBeInsertedApp : toBeInsertedApps) {
            Optional<ApplicationEntity> insertedOptApp = JdbcApplicationDao.insertApplication(toBeInsertedApp, connection);
            if (insertedOptApp.isEmpty() || !insertedOptApp.get().equals(toBeInsertedApp)) {
                throw new JdbcDaoRuntimeException("Could not insert: " + toBeInsertedApp + ". Inserted: " + insertedOptApp);
            }
        }
    }

    private static void insertApplicationIdToReleaseIds(ReleaseEntity toBeInsertedRelease, Connection connection) throws SQLException {
        for (ApplicationEntity toBeInsertedApp : toBeInsertedRelease.getApplications()) {
            Optional<AbstractMap.SimpleEntry<Integer, Integer>> insertedOptAppIdToReleaseId =
                    JdbcApplicationToReleaseDao.insertApplicationIdToReleaseId(toBeInsertedApp.getId(), toBeInsertedRelease.getId(), connection);
            if (insertedOptAppIdToReleaseId.isEmpty()
                    || !insertedOptAppIdToReleaseId.get().getKey().equals(toBeInsertedApp.getId())
                    || !insertedOptAppIdToReleaseId.get().getValue().equals(toBeInsertedRelease.getId())) {
                throw new JdbcDaoRuntimeException("Could not insert: " +
                        new AbstractMap.SimpleEntry<>(
                                Constants.Tables.ApplicationsToReleases.APPLICATION_ID + ": " + toBeInsertedApp.getId(),
                                Constants.Tables.ApplicationsToReleases.RELEASE_ID + ": " + toBeInsertedRelease.getId()) +
                        ". Inserted: " + insertedOptAppIdToReleaseId);
            }
        }
    }
}
