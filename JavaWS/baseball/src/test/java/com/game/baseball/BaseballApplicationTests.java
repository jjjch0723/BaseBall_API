package com.game.baseball;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseballApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testProcessSchedule() {
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/schedule/process", null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Schedule processing started");
    }

    @Test
    void testProcessResults() {
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/results/process", null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Results processing started");
    }

    @Test
    void testGetAppVersion() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/version", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Application version: 1.0.0");
    }
}
