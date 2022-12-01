package io.github.sadiqs.robot.web;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RobotIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders requestHeaders;

    @PostConstruct
    void init() {
        requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, TEXT_PLAIN_VALUE);
    }


    @Test
    void shouldReportInitialLocation() {

        String response = restTemplate.postForObject("/robot", new HttpEntity<>("""
                PLACE 3,4,NORTH
                REPORT""", requestHeaders), String.class);

        assertThat(response).isEqualTo("3,4,NORTH");
    }

    @Test
    void shouldIgnoreRequestsBeforeInitialPlacement() {

        String response = restTemplate.postForObject("/robot", new HttpEntity<>("""
                MOVE
                PLACE 3,4,NORTH
                REPORT""", requestHeaders), String.class);

        assertThat(response).isEqualTo("3,4,NORTH");
    }

    @Test
    void shouldIgnoreAllRequestsBeforeInitialPlacement() {

        String response = restTemplate.postForObject("/robot", new HttpEntity<>("""
                MOVE
                LEFT
                LEFT
                MOVE
                RIGHT
                REPORT""", requestHeaders), String.class);

        assertThat(response == null);
    }

    @Test
    void shouldIgnoreInstructionsLeadingOffTheEdge() {

        String response = restTemplate.postForObject("/robot", new HttpEntity<>("""
                PLACE 1,1,EAST
                MOVE
                MOVE
                MOVE
                MOVE
                MOVE
                MOVE
                MOVE
                REPORT
                LEFT
                MOVE
                MOVE
                MOVE
                REPORT""", requestHeaders), String.class);

        assertThat(response).isEqualTo("""
                4,1,EAST
                4,4,NORTH""");
    }

    @Test
    void shouldPerformMultipleSteps() {
        var scenarios = Map.of("""
                        PLACE 0,0,NORTH
                        MOVE
                        REPORT""", "0,1,NORTH",
                """
                        PLACE 0,0,NORTH
                        LEFT
                        REPORT""", "0,0,WEST",
                """
                        PLACE 1,2,EAST
                        MOVE
                        MOVE
                        LEFT
                        MOVE
                        REPORT""", "3,3,NORTH");

        scenarios.forEach((instructions, expectedResult) -> {
            String response = restTemplate.postForObject("/robot", new HttpEntity<>(instructions, requestHeaders), String.class);

            assertThat(response).isEqualTo(expectedResult);
        });
    }
}
