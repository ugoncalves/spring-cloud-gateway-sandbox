package com.example.scgsandbox.route;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {8787, 8888})
public class TestRouteUsingMockServer {

    static ClientAndServer clientAndServer;
    static MockServerClient mockServerClient;
    WebTestClient gateway;
    @LocalServerPort int gatewayPort;

    public TestRouteUsingMockServer(ClientAndServer clientAndServer) {
        TestRouteUsingMockServer.clientAndServer = clientAndServer;
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("TENANT", () -> "http://localhost:"+clientAndServer.getLocalPort());
    }

    private static void configureMockServerClient(HttpMethod method, String path) {
        mockServerClient = new MockServerClient("localhost", clientAndServer.getLocalPort());
        mockServerClient.when(
                        HttpRequest.request()
                                .withMethod(method.name())
                                .withPath(path)
                )
                .respond(r -> {
                    return HttpResponse.response()
                            .withBody("Hello from mockServer")
                            .withStatusCode(200);
                });
    }

    @BeforeEach
    void setUp() {
        gateway = WebTestClient.bindToServer().baseUrl("http://localhost:" + gatewayPort).build();
    }

    @AfterAll
    static void stopMockServer() {
        clientAndServer.stop();
    }

    private static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of(HttpMethod.GET, "/sso2/api/users/", 200, "/api/users/"),
                Arguments.of(HttpMethod.GET, "/sso2/api/users/", 200, "/api/users/"),
                Arguments.of(HttpMethod.GET, "/sso2/api/users/", 200, "/api/users/")
        );
    }

    @ParameterizedTest
    @MethodSource("provideData")
    void test(HttpMethod method, String uri, int expectedStatus, String expectedPath) {

        configureMockServerClient(method, expectedPath);

        WebTestClient.RequestBodySpec webTestClient = gateway.method(method).uri(uri);

        webTestClient
                .exchange()
                .expectStatus()
                .isEqualTo(expectedStatus)
                .expectBody(String.class)
                .isEqualTo("Hello from mockServer");
    }


}
