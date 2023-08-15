# BugTrackerSpringJpaH2

Spring + JPA + H2 DB - Demonstration

### DB Schema

| Original DB Schema                                                             | Modified DB Schema                   |
|--------------------------------------------------------------------------------|--------------------------------------|
| ![Original DB schema](./src/main/resources/db_schema_before_modifications.png) | ![Modified DB Schema - TODO](./todo) |

### Application run

1. Compile package  
   `mvn clean package`
    1. Add `-DskipTests` flag to skip run of tests
    2. Add `-DparallelTests=false` flag to force tests to run sequentially. \
       Property `parallelTests` is set to `true` by
       default in [pom.xml](pom.xml). \
       Additionaly, [junit-platform.properties](src%2Ftest%2Fresources%2Fjunit-platform.properties) also
       have `junit.jupiter.execution.parallel.enabled=true`.
2. Execute jar package  
   `java -jar ./target/BugTrackerSpringJpaH2_complete_standalone.jar`
3. Access application
    1. [Application status endpoint](http://localhost:8080/bug-tracker/status)
    2. [DB Console](http://localhost:8080/console/)

### Other information

1. [application.properties](src%2Fmain%2Fresources%2Fapplication.properties) Various application properties (including
   Database Datasource
   setting)

### Git

1. Clone remote repository to local drive  
   `git clone https://github.com/axal25/BugTrackerSpringJpaH2.git`
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

### Courses

#### Done courses:

1. [Created based on course: Java Persistence API (JPA): 1 The Basics by Kesha Williams](https://www.linkedin.com/learning/java-persistence-api-jpa-1-the-basics/)

#### TO DO courses:

1. [Java Persistence API (JPA): 2 Inheritance and Querying by Kesha Williams](https://www.linkedin.com/learning/java-persistence-api-jpa-2-inheritance-and-querying)
2. [Kesha Williams](https://www.linkedin.com/learning/instructors/kesha-williams?u=2113185)
3. [Java-rock-star Kesha Williams](https://www.linkedin.com/in/java-rock-star-kesha)
4. [Java Persistence with JPA](https://www.linkedin.com/learning/java-persistence-with-jpa/map-java-objects-to-databases?autoplay=true&u=2113185)
5. [Java EE: Context and Dependency Injection](https://www.linkedin.com/learning/java-ee-contexts-and-dependency-injection/welcome?autoplay=true&u=2113185)
6. [Java EE: Bean Validation](https://www.linkedin.com/learning/java-ee-bean-validation/welcome?autoplay=true&u=2113185)
7. [Java EE: Design Patterns and Architecture](https://www.linkedin.com/learning/java-ee-design-patterns-and-architecture/advantages-and-disadvantages-of-microservices?autoplay=true&u=2113185)
8. [Java EE: JSON processing](https://www.linkedin.com/learning/json-processing-with-java-ee/welcome?autoplay=true&u=2113185)
9. [Java EE: WebSocket Programming](https://www.linkedin.com/learning/websocket-programming-with-java-ee/welcome?autoplay=true&u=2113185)
5. Hibernate
5. Spring MVC

### TODO:

1. Jdbc prefilling of DB - replace with Jdbc Dao logic
    1. Causes some tests to fail
2. Rename DB's tables, fields. Add screenshot of new DB schema.
3. (Not sure if) Add Bidirectional references to entities where only unidirectional exist
4. Finish ITicketDaoTester
    2. Test failures:
        3. Test runs count: 60
            1. Fix TicketCrudRepository tests
                1. #addTicket_isSuccessfullyAdded
                    1. Failures count: 12
                2. #getAllTickets_containsAddedTicket
                    1. Failures count: 4
            2. JdbcTicketDaoTest
                1. #selectTickets()
                    1. with Prefiller#prefillInMemDb
                        1. Failures count: 2
            3. JdbcApplicationToReleaseDaoTest
                1. #selectApplicationIdToReleaseIdMapByReleaseIds
                    1. with Prefiller#prefillInMemDb
                        1. Failures count: 2
        2. Test runs count: 3
            1. TicketCrudRepository - newly broken - trace: TicketCrudRepositor#addTicket <
               ITicketDaoTester#addAnsAssertTicket
                1. #addTicket_isSuccessfullyAdded
                    1. Failures count: 0
                2. #getAllTickets_containsAddedTicket_skippingLazilyFetchedEntities
                    1. Failures count: 2
                3. #getAllTicketsEagerly_containsAddedTicket_includingLazilyFetchedEntites
                    1. Failures count: 2
                4. #getTicketById_isEqulToAddedTicket_skippingLazilyFetchedEntities
                    1. Failures count: 3
                5. #getTicketById_eagerly_isEqualToAddedTicket_includingLaziltyFetchedEntities
                    1. Failures count: 3
                6. #deleteTicket_deletesAddedTicket
                    1. Failures count: 3
                7. #updateTicket_updatesDescriptionAndTitle
                    1. Failure count: 2
                8. #closeTicket_updatesStatusToResolvedValue
                    1. Failures count: 0
2. Id Generation for entities is 'not nice'. Static access to `ApplicationContext`. Rewrite.
5. Remove `DatabaseUtils.getConnection()`?
    1. At least use EntityManager's Session in Non-Transactional-Jdbc Tests instead?
    2. Remove Id Generation Method: FROM_JDBC?
6. Make use of Dtos
    1. field validation
        1. @NotNull
        2. @Pattern
7. Cache-ing
8. Add Swagger

### Known issues:

1. Missing libraries - print exception, does not block execution \
   Install missing libraries using: \
   `sudo apt-get install libtcnative-1`
2. Java version pre 19 \
   `o.apache.tomcat.util.compat.Jre19Compat  : Class not found so assuming code is running on a pre-Java 19 JVM`
    1. `sudo apt update`
    2. `sudo apt search openjdk` \
       Output:
   ```text
    openjdk-19-jdk/jammy-updates,jammy-security 19.0.2+7-0ubuntu3~22.04 amd64
    OpenJDK Development Kit (JDK)
    ```
    3. `sudo apt install openjdk-19-jdk`
    4. `sudo update-alternatives --config java` \
       Output:
   ```text
    There are 3 choices for the alternative java (providing /usr/bin/java).
    
    Selection    Path                                         Priority   Status
    ------------------------------------------------------------
      0            /usr/lib/jvm/java-19-openjdk-amd64/bin/java   1911      auto mode
      1            /usr/lib/jvm/java-11-openjdk-amd64/bin/java   1111      manual mode
    * 2            /usr/lib/jvm/java-17-openjdk-amd64/bin/java   1711      manual mode
      3            /usr/lib/jvm/java-19-openjdk-amd64/bin/java   1911      manual mode
    
    Press <enter> to keep the current choice[*], or type selection number: 0
    update-alternatives: using /usr/lib/jvm/java-19-openjdk-amd64/bin/java to provide /usr/bin/java (java) in auto mode
   ```
    5. `sudo update-alternatives --config javac`
    6. `which java` \
       Output: `/usr/bin/java`
    7. `whereis java` \
       Output: `java: /usr/bin/java /usr/share/java /usr/share/man/man1/java.1.gz`
    8. `ls -l /usr/bin/java` \
       Output: `lrwxrwxrwx 1 root root 22 wrz 29  2019 /usr/bin/java -> /etc/alternatives/java`
    9. `ls -l /etc/alternatives/java` \
       Ouput: `lrwxrwxrwx 1 root root 43 cze 28 16:40 /etc/alternatives/java -> /usr/lib/jvm/java-19-openjdk-amd64/bin/java`
    10. `ls -l /usr/lib/jvm/default-java` \
        Output: `lrwxrwxrwx 1 root root 25 lut 20  2019 /usr/lib/jvm/default-java -> java-1.11.0-openjdk-amd64`
    11. `sudo ln -s /usr/bin/java /usr/lib/jvm/default-java`
    12. `ls -l /usr/lib/jvm/default-java` \
        Output: `lrwxrwxrwx 1 root root 13 cze 28 16:49 /usr/lib/jvm/default-java -> /usr/bin/java`
    13. `update-alternatives --install /usr/bin/java java $PATH $PRIORITY`
    14. `sudo nano /etc/environment` \
        Remove `$JAVA_HOME` from:
   ```text
   PATH="<[...]>:$JAVA_HOME:<[...]>"
   ```
    15. `sudo nano /etc/profile` \
        Add to the end of file:
   ```text
   if [ -d /etc/profile.d ]; then
     for i in /etc/profile.d/*.env; do
       if [ -r $i ]; then
         source $i
       fi
     done
     unset i
   fi
   ```
   So it looks something like this:
   ```text
   # /etc/profile: system-wide .profile file for the Bourne shell (sh(1))
   # and Bourne compatible shells (bash(1), ksh(1), ash(1), ...).

   if [ "${PS1-}" ]; then
     if [ "${BASH-}" ] && [ "$BASH" != "/bin/sh" ]; then
     # The file bash.bashrc already sets the default PS1.
     # PS1='\h:\w\$ '
       if [ -f /etc/bash.bashrc ]; then
         . /etc/bash.bashrc
       fi
     else
       if [ "$(id -u)" -eq 0 ]; then
         PS1='# '
       else
         PS1='$ '
       fi
     fi
   fi

   if [ -d /etc/profile.d ]; then
     for i in /etc/profile.d/*.sh; do
       if [ -r $i ]; then
         . $i
       fi
     done
     unset i
   fi
   # newly added
   if [ -d /etc/profile.d ]; then
     for i in /etc/profile.d/*.env; do
       if [ -r $i ]; then
         source $i
       fi
     done
     unset i
   fi
   ```
    16. `sudo nano /etc/profile.d/java_home.env` and add contents:
   ```text
   #!/bin/bash

   tmp=$(eval "readlink -f '/usr/lib/jvm/default-java'")
   # exit [...]/bin/java
   # exit [...]/java
   tmp=$(dirname "${tmp}")
   # exit [...]/bin
   tmp=$(dirname "${tmp}")
   export JAVA_HOME="${tmp}"
   unset tmp
   ```
3. Another issue 