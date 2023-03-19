package axal25.oles.jacek;

import axal25.oles.jacek.dao.mock.Prefiller;
import axal25.oles.jacek.log.EndpointPrinter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BugTrackerApp {
    // http://localhost:8080/console
    // http://localhost:8080/bug-tracker/status
    // http://jackdaeel-lt-ubuntu:8080/bug-tracker/status
    public static void main(String[] args) {
        SpringApplication.run(BugTrackerApp.class, args);
        Prefiller.prefillInMemDb();
        EndpointPrinter.printEndpoints();
        // TODO: Finished at
        //  https://www.linkedin.com/learning/java-persistence-api-jpa-1-the-basics/mapping-strategies-overview?autoSkip=true&autoplay=true&resume=false&u=2113185
        //  4. Relationship Mapping in JPA > 4.1. Relationships
        // TODO: BVNK interview assessment / recruitment
        //  1) Dto + Field Validation (@Pattern) - https://stackoverflow.com/questions/9978199/annotation-regex
        //  2) CrudRepository
        //  3) @Query (Native vs. JPQL) - https://www.baeldung.com/spring-data-jpa-query
        //  4) Exception handling - https://www.baeldung.com/exception-handling-for-rest-with-spring
    }
}
