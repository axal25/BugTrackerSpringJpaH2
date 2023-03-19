# BugTrackerSpringJpaH2
Spring + JPA + H2 DB - Demonstration

[Created based on course: Java Persistence API (JPA): 1 The Basics by Kesha Williams](https://www.linkedin.com/learning/java-persistence-api-jpa-1-the-basics/)  
[Todo course: Java Persistence API (JPA): 2 Inheritance and Querying by Kesha Williams](https://www.linkedin.com/learning/java-persistence-api-jpa-2-inheritance-and-querying)

### DB Schema
| Original DB Schema                                                             | Modified DB Schema                   |
|--------------------------------------------------------------------------------|--------------------------------------|
| ![Original DB schema](./src/main/resources/db_schema_before_modifications.png) | ![Modified DB Schema - TODO](./todo) |

### Application run

1. Compile package  
`mvn clean package -DskipTests`
   1. Or leave out `-DskipTests` flag to also run tests
2. Execute jar package  
`java -jar ./target/BugTrackerSpringJpaH2_complete_standalone.jar`
3. Access application  
   1. [Application status endpoint](http://localhost:8080/bug-tracker/status)  
   2. [DB Console](http://localhost:8080/console/)

### TODO:
1. Jdbc prefilling of DB
2. Rename DB's tables, fields. Add screenshot of new DB schema.
3. Add Bidirectional references to entities where only unidirectional exist
4. Finish Non-Jta Ticket Dao
5. Have Ticket Dao Test run for all implementations (Jta, Non-Jta, SpringCrudRepository)
6. Finish TickerCrudRepositoryTest

### Git

1. Clone remote repository to local drive  
   `git clone https://github.com/axal25/JavaCourse.git`
2. Make changes
3. Add changes to the index  
   `git add .`
4. Verify the added changes  
   `git status`
6. Commit changes with message  
   `git commit -m 'msg'`
7. Verify commited changes  
   `git status`
8. Push changes made to remote repository branch called **main**    
   `git push -u origin main`

### Pass

1. Copy token to clipboard  
    `pass -c Git/axal25`