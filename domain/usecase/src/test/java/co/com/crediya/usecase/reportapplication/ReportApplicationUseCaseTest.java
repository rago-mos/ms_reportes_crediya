package co.com.crediya.usecase.reportapplication;

import co.com.crediya.model.exception.DynamoException;
import co.com.crediya.model.reportloan.ReportLoan;
import co.com.crediya.model.reportloan.dto.NotificationData;
import co.com.crediya.model.reportloan.gateways.ReportLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportApplicationUseCaseTest {

    @Mock
    private ReportLoanRepository reportLoanRepository;

    private ReportApplicationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReportApplicationUseCase(reportLoanRepository);
    }

    @Test
    void shouldReturnReportLoanSuccessfully() {

        ReportLoan report = ReportLoan.builder()
                .totalLoan(10L)
                .totalAmount(new BigDecimal("5000"))
                .build();

        when(reportLoanRepository.getReport()).thenReturn(Mono.just(report));

        Mono<ReportLoan> result = useCase.getReportLoan();

        StepVerifier.create(result)
                .expectNextMatches(r -> r.getTotalLoan().equals(10L) &&
                        r.getTotalAmount().compareTo(new BigDecimal("5000")) == 0)
                .verifyComplete();

        verify(reportLoanRepository).getReport();
    }

    @Test
    void shouldUpdateReportLoanSuccessfully() {

        NotificationData data = NotificationData.builder()
                .idStatus(1)
                .amount(new BigDecimal("2500"))
                .build();

        when(reportLoanRepository.updateReport(data.getAmount())).thenReturn(Mono.empty());

        Mono<Void> result = useCase.updateReportLoan(data);

        StepVerifier.create(result).verifyComplete();
        verify(reportLoanRepository).updateReport(new BigDecimal("2500"));
    }

    @Test
    void shouldPropagateErrorFromGetReport() {

        when(reportLoanRepository.getReport()).thenReturn(Mono.error(new DynamoException("fail")));

        Mono<ReportLoan> result = useCase.getReportLoan();

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DynamoException && e.getMessage().equals("fail"))
                .verify();
    }

    @Test
    void shouldPropagateErrorFromUpdateReport() {

        NotificationData data = NotificationData.builder()
                .idStatus(2)
                .amount(new BigDecimal("999"))
                .build();

        when(reportLoanRepository.updateReport(data.getAmount()))
                .thenReturn(Mono.error(new RuntimeException("update failed")));

        Mono<Void> result = useCase.updateReportLoan(data);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().equals("update failed"))
                .verify();
    }
}