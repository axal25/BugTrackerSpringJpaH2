package axal25.oles.jacek.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static String lengthyElementsToString(Collection<String> list) {
        return list.stream()
                .map(Object::toString)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.joining(",\r\n "),
                                joinedCommaNlSeparated -> String.format("[\r\n%s\r\n]", joinedCommaNlSeparated)));
    }
}
