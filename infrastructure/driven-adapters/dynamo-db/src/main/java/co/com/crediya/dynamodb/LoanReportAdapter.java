package co.com.crediya.dynamodb;

import co.com.crediya.model.exception.DynamoException;
import co.com.crediya.model.reportloan.ReportLoan;
import co.com.crediya.model.reportloan.gateways.ReportLoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.math.BigDecimal;
import java.util.Map;

import static co.com.crediya.model.utils.Constant.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoanReportAdapter implements ReportLoanRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @Override
    public Mono<ReportLoan> getReport() {
        var getRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(ID_TABLE, AttributeValue.fromS(ID_TABLE_VALUE)))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.getItem(getRequest))
                .map(response -> {
                    if (response.item() == null || response.item().isEmpty()) return new ReportLoan(0L, BigDecimal.ZERO);

                    return ReportLoan.builder()
                            .totalLoan(Long.parseLong(response.item().get(TOTAL_LOAN).n()))
                            .totalAmount(new BigDecimal(response.item().get(TOTAL_AMOUNT).n()))
                            .build();
                })
                .onErrorMap(e -> new DynamoException(ERROR_DYNAMO_GET));
    }

    @Override
    public Mono<Void> updateReport(BigDecimal amountAdd) {
        var updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(ID_TABLE, AttributeValue.fromS(ID_TABLE_VALUE)))
                .updateExpression("SET total_loan = if_not_exists(total_loan, :zero) + :addLoan, " +
                        "total_amount = if_not_exists(total_amount, :zero) + :addAmount")
                .expressionAttributeValues(Map.of(
                        ":addLoan", AttributeValue.fromN("1"),
                        ":addAmount", AttributeValue.fromN(amountAdd.toPlainString()),
                        ":zero", AttributeValue.fromN("0")
                ))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.updateItem(updateRequest))
                .doOnSuccess(v -> log.info(LOG_INFO_GET_REPORT))
                .doOnError(e -> log.error(ERROR_DYNAMO_UPDATED, e.getMessage()))
                .then();
    }
}
