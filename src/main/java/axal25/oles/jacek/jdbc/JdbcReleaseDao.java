package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.constant.Constants;
import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcReleaseDao {
    private final Connection connection = DatabaseUtils.getConnection();
    private final JdbcApplicationDao appDao = new JdbcApplicationDao();
    private final JdbcApplicationToReleaseDao appToReleaseDao = new JdbcApplicationToReleaseDao();

    public JdbcReleaseDao() throws SQLException {
    }

    public List<ReleaseEntity> selectReleases() throws SQLException {
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

        return getReleasesWithUpdatedApplications(releases);
    }

    private List<ReleaseEntity> getReleasesWithUpdatedApplications(List<ReleaseEntity> releases) throws SQLException {
        Map<Integer, Integer> applicationIdToReleaseIds = appToReleaseDao.selectApplicationIdToReleaseIdMapByReleaseIds(
                releases.stream().map(ReleaseEntity::getId).collect(Collectors.toList()));
        List<ApplicationEntity> applications = appDao.selectApplicationsByIds(
                List.copyOf(applicationIdToReleaseIds.keySet()));

        releases = releases.stream()
                .peek(release -> {
                    Set<Integer> applicationIds = applicationIdToReleaseIds.entrySet().stream()
                            .filter(applicationIdToReleaseId -> release.getId()
                                    .equals(applicationIdToReleaseId.getValue()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
                    List<ApplicationEntity> releaseApplications = applications.stream()
                            .filter(application -> applicationIds.contains(application.getId()))
                            .collect(Collectors.toList());
                    release.setApplications(releaseApplications);
                })
                .collect(Collectors.toList());

        return releases;
    }

    public Optional<ReleaseEntity> insertRelease(ReleaseEntity release) throws SQLException {
        insertApplications(release);

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

        insertApplicationIdToReleaseIds(release);

        return Optional.of(release);
    }

    private void insertApplications(ReleaseEntity release) throws SQLException {
        List<ApplicationEntity> existingApps = appDao.selectApplicationsByIds(
                release.getApplications().stream()
                        .map(ApplicationEntity::getId)
                        .collect(Collectors.toList()));
        List<ApplicationEntity> toBeInsertedApps = release.getApplications().stream()
                .filter(app -> !existingApps.contains(app))
                .collect(Collectors.toList());
        for (ApplicationEntity toBeInsertedApp : toBeInsertedApps) {
            Optional<ApplicationEntity> inserted = appDao.insertApplication(toBeInsertedApp);
            if (inserted.isEmpty() || !inserted.get().equals(toBeInsertedApp)) {
                throw new SQLException("Could not insert: " + toBeInsertedApp);
            }
        }
    }

    private void insertApplicationIdToReleaseIds(ReleaseEntity release) throws SQLException {
        for (ApplicationEntity application : release.getApplications()) {
            Optional<AbstractMap.SimpleEntry<Integer, Integer>> optionalApplicationIdToReleaseId =
                    appToReleaseDao.insertApplicationIdToReleaseId(application.getId(), release.getId());
            if (optionalApplicationIdToReleaseId.isEmpty()
                    || !optionalApplicationIdToReleaseId.get().getKey().equals(application.getId())
                    || !optionalApplicationIdToReleaseId.get().getValue().equals(release.getId())) {
                throw new SQLException("Could not insert: " +
                        new AbstractMap.SimpleEntry<>(
                                Constants.Tables.ApplicationsToReleases.APPLICATION_ID + ": " + application.getId(),
                                Constants.Tables.ApplicationsToReleases.RELEASE_ID + ": " + release.getId()));
            }
        }
    }
}
