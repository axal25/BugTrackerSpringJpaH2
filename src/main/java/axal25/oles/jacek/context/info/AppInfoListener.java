package axal25.oles.jacek.context.info;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AppInfoListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(BugTrackerAppStatusProducer.class);
    private static final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(BugTrackerAppStatusProducer.class);
    private final Environment environment;
    private final BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady;

    @Autowired
    public AppInfoListener(Environment environment) {
        this.environment = environment;
        this.bugTrackerAppStatusProducerOnApplicationReady = new BugTrackerAppStatusProducerOnApplicationReady();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        bugTrackerAppStatusProducerOnApplicationReady
                .setBugTrackerAppStatusProducer(
                        new BugTrackerAppStatusProducer(environment));
        printState();
    }

    @Bean
    public BugTrackerAppStatusProducerOnApplicationReady provideBugTrackerAppStatusProducerOnApplicationReady() {
        return bugTrackerAppStatusProducerOnApplicationReady;
    }

    public void printState() {
        Stream.of(
                        Stream.of("", "System.out::println"),
                        bugTrackerAppStatusProducerOnApplicationReady
                                .getBugTrackerAppStatusProducer()
                                .toStateStringStream())
                .flatMap(i -> i)
                .forEach(System.out::println);
        Stream.of(
                        Stream.of("", "org.slf4j.Logger::info"),
                        bugTrackerAppStatusProducerOnApplicationReady
                                .getBugTrackerAppStatusProducer()
                                .toStateStringStream())
                .flatMap(i -> i)
                .forEach(slf4jLogger::info);
        Stream.of(
                        Stream.of("", "org.apache.logging.log4j.Logger::info"),
                        bugTrackerAppStatusProducerOnApplicationReady
                                .getBugTrackerAppStatusProducer()
                                .toStateStringStream())
                .flatMap(i -> i)
                .forEach(log4jLogger::info);
    }
}
