package axal25.oles.jacek.entity.factory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodNameValidator {

    public static String getValidatedMethodName(String expectedMethodName, Class<?> methodOwnerClass) {
        List<String> matchingMethodNames = Arrays.stream(methodOwnerClass.getMethods())
                .map(Method::getName)
                .filter(name -> name.equals(expectedMethodName))
                .collect(Collectors.toList());

        if (matchingMethodNames.isEmpty()) {
            throw new IllegalArgumentException("No matching method name found");
        }

        if (matchingMethodNames.size() > 1) {
            throw new IllegalArgumentException("More than 1 matching method name found: [" +
                    String.join(", ", matchingMethodNames) +
                    "].");
        }

        return methodOwnerClass.getSimpleName()
                + "#" +
                matchingMethodNames.get(0) +
                "()";
    }
}
