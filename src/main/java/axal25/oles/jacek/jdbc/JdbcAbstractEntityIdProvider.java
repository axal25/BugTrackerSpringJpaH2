package axal25.oles.jacek.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class JdbcAbstractEntityIdProvider {

    private Set<Integer> fetched = null;
    private final Set<Integer> handedOut = new HashSet<>();

    protected JdbcAbstractEntityIdProvider() {
    }

    protected synchronized int generatedId() {
        Optional<Set<Integer>> optionalFreshIds = Optional.empty();
        try {
            optionalFreshIds = Optional.of(fetchIds());
        } catch (SQLException e) {
        }

        optionalFreshIds.ifPresent(freshIds -> fetched = freshIds);

        if (fetched == null) {
            throw new IllegalStateException("Couldn't fetch or use cached ids to generate new id.");
        }

        Set<Integer> combined = Stream.of(fetched, handedOut)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        int toBeHandedOut = IntStream.range(0, combined.size() + 1)
                .filter(counter -> !combined.contains(counter))
                .findFirst()
                .orElse(combined.size() + 1);

        handedOut.add(toBeHandedOut);

        return toBeHandedOut;
    }

    protected abstract Set<Integer> fetchIds() throws SQLException;
}
