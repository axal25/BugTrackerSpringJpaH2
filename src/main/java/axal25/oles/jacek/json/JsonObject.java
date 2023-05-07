package axal25.oles.jacek.json;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonObject {

    default String toJsonString() {
        try {
            return JsonProvider.getObjectMapper()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Couldn't produce Json for object: " + this);
            throw new RuntimeException(e);
        }
    }

    default String toJsonSpacedString() {
        try {
            return JsonProvider.getObjectMapper()
                    .writer(JsonProvider.writerWithSpaces())
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Couldn't produce Json for object: " + this);
            throw new RuntimeException(e);
        }
    }

    default String toJsonPrettyString() {
        try {
            return JsonProvider.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Couldn't produce Json for object: " + this);
            throw new RuntimeException(e);
        }
    }
}
