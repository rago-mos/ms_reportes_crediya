package co.com.crediya.api;

import co.com.crediya.model.reportloan.ReportLoan;
import co.com.crediya.usecase.reportapplication.IReportApplicationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @Mock
    private IReportApplicationUseCase reportApplicationUseCase;

    private Handler handler;

    @BeforeEach
    void setUp() {
        handler = new Handler(reportApplicationUseCase);
    }

    @Test
    void shouldReturnServerResponseWithReportLoan() {

        ReportLoan reportLoan = ReportLoan.builder()
                .totalLoan(5L)
                .totalAmount(new BigDecimal("10000.00"))
                .build();

        when(reportApplicationUseCase.getReportLoan()).thenReturn(Mono.just(reportLoan));

        ServerRequest mockRequest = mock(ServerRequest.class);

        Mono<ServerResponse> result = handler.listenGETUseCase(mockRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(response.headers().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();

        verify(reportApplicationUseCase).getReportLoan();
    }

    @Test
    void shouldPropagateErrorIfUseCaseFails() {

        when(reportApplicationUseCase.getReportLoan()).thenReturn(Mono.error(new RuntimeException("DB error")));

        ServerRequest mockRequest = mock(ServerRequest.class);

        Mono<ServerResponse> result = handler.listenGETUseCase(mockRequest);

        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("DB error"))
                .verify();

        verify(reportApplicationUseCase).getReportLoan();
    }
}
