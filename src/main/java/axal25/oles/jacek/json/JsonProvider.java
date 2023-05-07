package axal25.oles.jacek.json;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProvider {

    private JsonProvider() {
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
                .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
    }

    public static DefaultPrettyPrinter writerWithSpaces() {
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("", " ");
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter("");
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        return printer;
    }
}
