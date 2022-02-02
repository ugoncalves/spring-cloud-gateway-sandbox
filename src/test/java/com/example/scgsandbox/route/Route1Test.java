package com.example.scgsandbox.route;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
//@SpringBootTest(webEnvironment = RANDOM_PORT)
public class Route1Test {

    @Container
    private final GenericContainer<?> topLevelContainer = new GenericContainer<>(
            DockerImageName.parse("jamesdbloom/mockserver"));

//    private final GenericContainer<?> topLevelContainer = new GenericContainer(
//        new ImageFromDockerfile().withDockerfileFromBuilder(builder ->
//            builder.from("alpine:3.14").run("apk add --update nginx").cmd("nginx", "-g", "daemon off;").build()))
//            .withExposedPorts(80);

    @Test
    void top_level_container_should_be_running() {
        assertTrue(topLevelContainer.isRunning());

    }
}
