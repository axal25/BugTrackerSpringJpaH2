package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.constant.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
public class JdbcQueryTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseUtils.getConnection();
    }

    @Test
    void jdbcSmokeTest() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:" + "non-existent-db", new Properties());
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT '" + "connected" + "'");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString(1)).isEqualTo("connected");
    }

    @Test
    void doesTableApplicationsExist1() throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, Constants.Tables.APPLICATIONS.toUpperCase());

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        assertThat(resultSet.getInt(1)).isNotEqualTo(0);
    }

    @Test
    void doesTableApplicationsExist2() throws Exception {
        String applicationsTableName = Constants.Tables.APPLICATIONS.toUpperCase();
        ResultSet resultSet = DatabaseMetaDataUtils.getTables(
                connection,
                null,
                null,
                applicationsTableName,
                new String[]{"TABLE"});
        assertThat(resultSet.next()).isTrue();
        assertThat(DatabaseMetaDataUtils.read(resultSet).getTableName())
                .isEqualTo(applicationsTableName);
    }

    @Test
    void doTablesExist() throws Exception {
        ResultSet resultSet = DatabaseMetaDataUtils.getTables(
                connection,
                null,
                "PUBLIC",
                null,
                new String[]{"TABLE"});
        List<DatabaseMetaDataUtils.GetTablesRow> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(DatabaseMetaDataUtils.read(resultSet));
        }
        assertThat(results.stream()
                .map(DatabaseMetaDataUtils.GetTablesRow::getTableName)
                .collect(Collectors.toList()))
                .containsExactlyElementsIn(
                        Stream.of(Constants.Tables.APPLICATIONS,
                                        Constants.Tables.RELEASES,
                                        Constants.Tables.APPLICATIONS_TO_RELEASES,
                                        Constants.Tables.TICKETS,
                                        Constants.Tables.RELEASES_TO_TICKETS)
                                .map(String::toUpperCase)
                                .collect(Collectors.toList()));
    }
}
