package axal25.oles.jacek.jdbc.id.provider;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractEntityIdProvider {

    private final Set<Integer> handedOut = new HashSet<>();

    protected AbstractEntityIdProvider() {
    }

    protected synchronized int instanceGenerateId() {
        Set<Integer> freshIds;
        try {
            freshIds = fetchIds();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't fetch Ids.", e);
        }

        Set<Integer> combined = Stream.of(freshIds, handedOut)
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
