package axal25.oles.jacek.context.info;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static axal25.oles.jacek.constant.Constants.EndpointPaths.*;
import static axal25.oles.jacek.constant.Constants.EndpointPaths.WebEndpointPaths.APPLICATION_WEB_CONTROLLER;

@Component
public class EndpointsProvider implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(EndpointsProvider.class);
    private static final org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(EndpointsProvider.class);

    private final BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady;
    private Map<String, String> descriptionToEndpointMap;

    @Autowired
    public EndpointsProvider(
            BugTrackerAppStatusProducerOnApplicationReady bugTrackerAppStatusProducerOnApplicationReady) {
        this.bugTrackerAppStatusProducerOnApplicationReady = bugTrackerAppStatusProducerOnApplicationReady;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initDescriptionToEndpointMap();
        printEndpoints();
    }

    private void initDescriptionToEndpointMap() {
        String fullHostAddress = bugTrackerAppStatusProducerOnApplicationReady
                .bugTrackerAppStatusProducer
                .getFullHostAddress() != null
                ? bugTrackerAppStatusProducerOnApplicationReady
                .bugTrackerAppStatusProducer
                .getFullHostAddress()
                : "http://localhost:8080";
        Map<String, String> tmp = new LinkedHashMap<>();
        tmp.put(
                "H2 Database Console",
                String.format("%s%s", fullHostAddress, H2_DB_CONSOLE));
        tmp.put(
                "Bug Tracker Application (API) status",
                String.format("%s%s/status", fullHostAddress, BUG_TRACKER_CONTROLLER));
        tmp.put(
                "Applications endpoints",
                String.format("%s%s", fullHostAddress, APPLICATION_CONTROLLER));
        tmp.put(
                "Releases endpoints",
                String.format("%s%s", fullHostAddress, RELEASE_CONTROLLER));
        tmp.put(
                "Tickets endpoints",
                String.format("%s%s", fullHostAddress, TICKET_CONTROLLER));
        tmp.put(
                "Applications Web",
                String.format("%s%s", fullHostAddress, APPLICATION_WEB_CONTROLLER));
        descriptionToEndpointMap = Collections.unmodifiableMap(tmp);
    }

    public void printEndpoints() {
        // TODO: Swagger
        Stream.of(Stream.of("", "System.out::println"), toEndpointStringStream())
                .flatMap(i -> i)
                .forEach(System.out::println);
        Stream.of(Stream.of("", "org.slf4j.Logger::info"), toEndpointStringStream())
                .flatMap(i -> i)
                .forEach(slf4jLogger::info);
        Stream.of(Stream.of("", "org.apache.logging.log4j.Logger::info"), toEndpointStringStream())
                .flatMap(i -> i)
                .forEach(log4jLogger::info);
    }

    private Stream<String> toEndpointStringStream() {
        return descriptionToEndpointMap
                .entrySet().stream()
                .map(descriptionToEndpoint -> String.format("[%s] %s",
                        descriptionToEndpoint.getKey(),
                        descriptionToEndpoint.getValue()));
    }

    public Map<String, String> getDescriptionToEndpointMap() {
        if (descriptionToEndpointMap == null) {
            throw new IllegalStateException("Wasn't initiated yet.");
        }
        return descriptionToEndpointMap;
    }
}
