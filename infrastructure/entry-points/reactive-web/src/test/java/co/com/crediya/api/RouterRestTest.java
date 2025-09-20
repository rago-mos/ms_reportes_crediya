package co.com.crediya.api;

import co.com.crediya.api.dto.response.GenericResponse;
import co.com.crediya.api.exception.GlobalExceptionHandler;
import co.com.crediya.usecase.reportapplication.IReportApplicationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, GlobalExceptionHandler.class})
@WebFluxTest
class RouterRestTest {

    private Handler handler;
    private RouterFunction<ServerResponse> routerFunction;
    private WebTestClient webTestClient;

    @MockitoBean
    private IReportApplicationUseCase reportApplicationUseCase;

    @BeforeEach
    void setUp() {
        handler = mock(Handler.class);
        routerFunction = new RouterRest().routerFunction(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRouteToHandler() {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status("CODE_001")
                .message("Report loan created successfully")
                .data("mock-data")
                .build();

        when(handler.listenGETUseCase(any())).thenReturn(
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
        );

        webTestClient.get()
                .uri("/api/v1/reportes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("CODE_001");
    }
}

@TestConfiguration
@EnableReactiveMethodSecurity
class SecurityConfigTest {

}
