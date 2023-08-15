package axal25.oles.jacek.util;

import axal25.oles.jacek.json.JsonObject;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static String lengthyStringToString(Stream<String> stream) {
        return stream
                .map(Object::toString)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.joining(",\r\n"),
                                joinedCommaNlSeparated -> String.format("[\r\n%s\r\n]", joinedCommaNlSeparated)));
    }

    public static String lengthyStringsToString(Collection<String> collection) {
        return lengthyStringToString(collection.stream());
    }

    public static <T extends JsonObject> String lengthyJsonObjectsToString(Collection<T> list) {
        return CollectionUtils.lengthyStringsToString(
                list.stream()
                        .map(jsonObject -> jsonObject == null
                                ? "null"
                                : jsonObject.toJsonPrettyString())
                        .collect(Collectors.toList()));
    }
}
