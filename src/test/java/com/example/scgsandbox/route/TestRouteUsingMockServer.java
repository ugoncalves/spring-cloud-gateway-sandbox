package com.example.scgsandbox.route;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestRouteUsingMockServer {

    static ClientAndServer mockServer;
    static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        mockServer = ClientAndServer.startClientAndServer(1080);
        mockServerClient = new MockServerClient("localhost", 1080);
        mockServerClient.when(
                        HttpRequest.request()
                                .withMethod("GET"))
                .respond(r -> {
                    return HttpResponse.response()
                            .withBody("Hello from mockServer")
                            .withStatusCode(200);
                });

        registry.add("TENANT", () -> "http://localhost:1080"); //mockServer.getHost()+":"+mockServer.getServerPort());
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }


    WebTestClient gateway;

    @LocalServerPort
    int gatewayPort;

    @Test
    void test() {
        gateway = WebTestClient.bindToServer().baseUrl("http://localhost:" + gatewayPort).build();

        gateway.method(HttpMethod.GET)
                .uri("/sso2/api/users/")
                .exchange()
                .expectStatus()
                .isEqualTo(200)
                .expectBody(String.class)
                .isEqualTo("Hello from mockServer");

    }
}
