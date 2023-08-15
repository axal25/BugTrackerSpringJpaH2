package axal25.oles.jacek.context.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextStaticInstanceProvider {

    private static ApplicationContext applicationContext;

    public ApplicationContextStaticInstanceProvider(@Autowired ApplicationContext applicationContext) {
        ApplicationContextStaticInstanceProvider.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        // TODO: https://foojay.io/today/statically-spilling-your-spring-beans/
        //  https://java-design-patterns.com/patterns/promise/
        if (applicationContext == null) {
            throw new RuntimeException(ApplicationContext.class.getSimpleName() + " is null. " +
                    "Tried to retrieve application context before application initialization.");
        }

        return applicationContext;
    }
}
