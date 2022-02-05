package com.example.scgsandbox.route;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestRouteUsingContainer {

//    @Container
//    private final GenericContainer<?> topLevelContainer = new GenericContainer<>(
//            DockerImageName.parse("jamesdbloom/mockserver"));

//    private final GenericContainer<?> topLevelContainer = new GenericContainer(
//        new ImageFromDockerfile().withDockerfileFromBuilder(builder ->
//            builder.from("alpine:3.14").run("apk add --update nginx").cmd("nginx", "-g", "daemon off;").build()))
//            .withExposedPorts(80);

    static ClientAndServer mockServer;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        mockServer = startClientAndServer(1080);

        registry.add("TENANT", ()->"http://localhost:1080" ); //mockServer.getHost()+":"+mockServer.getServerPort());

    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }



    @Test
    void test() {


        assertTrue(true);
        //assertTrue(topLevelContainer.isRunning());

    }
}
