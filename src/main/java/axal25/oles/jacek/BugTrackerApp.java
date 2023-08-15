package axal25.oles.jacek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class BugTrackerApp {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(BugTrackerApp.class, args);
        // TODO: Finished at
        //  https://www.linkedin.com/learning/java-persistence-api-jpa-1-the-basics/mapping-strategies-overview?autoSkip=true&autoplay=true&resume=false&u=2113185
        //  4. Relationship Mapping in JPA > 4.1. Relationships
        // TODO: BVNK interview assessment / recruitment
        //  1) Dto + Field Validation (@Pattern) - https://stackoverflow.com/questions/9978199/annotation-regex
        //  2) CrudRepository
        //  3) @Query (Native vs. JPQL) - https://www.baeldung.com/spring-data-jpa-query
        //  4) Exception handling - https://www.baeldung.com/exception-handling-for-rest-with-spring
    }

    private static class DBStudent {
        private int id;
        private String name;
        private List<String> lessons;
    }
}
