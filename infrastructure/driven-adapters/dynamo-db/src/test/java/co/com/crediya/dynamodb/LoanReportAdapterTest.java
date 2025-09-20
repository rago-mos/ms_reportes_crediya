package co.com.crediya.dynamodb;

import co.com.crediya.model.exception.DynamoException;
import co.com.crediya.model.reportloan.ReportLoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanReportAdapterTest {

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    private LoanReportAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new LoanReportAdapter(dynamoDbAsyncClient);
    }

    @Test
    void shouldReturnReportLoanWhenItemExists() {

        Map<String, AttributeValue> item = Map.of(
                "total_loan", AttributeValue.fromN("5"),
                "total_amount", AttributeValue.fromN("10000")
        );

        GetItemResponse response = GetItemResponse.builder().item(item).build();
        when(dynamoDbAsyncClient.getItem(any(GetItemRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        Mono<ReportLoan> result = adapter.getReport();

        StepVerifier.create(result)
                .assertNext(report -> {
                    assertThat(report.getTotalLoan()).isEqualTo(5L);
                    assertThat(report.getTotalAmount()).isEqualByComparingTo("10000");
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnZeroReportLoanWhenItemMissing() {

        GetItemResponse response = GetItemResponse.builder().item(Collections.emptyMap()).build();
        when(dynamoDbAsyncClient.getItem(any(GetItemRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        Mono<ReportLoan> result = adapter.getReport();

        StepVerifier.create(result)
                .expectNextMatches(report -> report.getTotalLoan() == 0L && report.getTotalAmount().compareTo(BigDecimal.ZERO) == 0)
                .verifyComplete();
    }

    @Test
    void shouldThrowDynamoExceptionOnGetError() {

        when(dynamoDbAsyncClient.getItem(any(GetItemRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("fail")));

        Mono<ReportLoan> result = adapter.getReport();

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DynamoException &&
                        e.getMessage().equals("An error occurred while querying the database"))
                .verify();
    }

    @Test
    void shouldUpdateReportSuccessfully() {

        UpdateItemResponse response = UpdateItemResponse.builder().build();
        when(dynamoDbAsyncClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        Mono<Void> result = adapter.updateReport(new BigDecimal("5000"));

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void shouldLogErrorOnUpdateFailure() {

        when(dynamoDbAsyncClient.updateItem(any(UpdateItemRequest.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("boom")));

        Mono<Void> result = adapter.updateReport(new BigDecimal("5000"));

        StepVerifier.create(result).verifyError(RuntimeException.class);
    }
}
