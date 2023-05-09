package axal25.oles.jacek;

import org.junit.jupiter.api.Test;
import org.springframework.boot.system.SystemProperties;
import org.springframework.core.SpringVersion;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;

public class VersionTest {
    private static final Pattern java_version_regex = Pattern.compile("^(1.)?([8-9]|[0-9][0-9]+)(\\..*)?$");
    private static final String[] java_version_expected_regex_matches = {"11", "8", "1.8", "1.11", "1.8.", "1.11.", "11.0.", "11.0.18", "1.8.0_191", "8.0_191", "11.0.18", "1.8.0_191", "8.0_191.0_9123.0231_.1239"};
    private static final String spring_version = "5.3.23";

    @Test
    void javaVersionRegex_isMatchingExpectedValues() {
        Arrays.stream(java_version_expected_regex_matches)
                .forEach(expected_match ->
                        assertThat(expected_match).matches(java_version_regex));
    }

    @Test
    void javaVersion_isEqualsToExpected_javaSystemProperty() {
        assertThat(System.getProperty("java.version")).matches(java_version_regex);
    }

    @Test
    void javaVersion_isEqualsToExpected_springSystemProperty() {
        assertThat(SystemProperties.get("java.version")).matches(java_version_regex);
    }

    @Test
    public void validateSpringVersion() {
        assertThat(SpringVersion.getVersion()).isEqualTo(spring_version);
    }
}
