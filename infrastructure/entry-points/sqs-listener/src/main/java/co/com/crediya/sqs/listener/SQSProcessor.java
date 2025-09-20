package co.com.crediya.sqs.listener;

import co.com.crediya.model.exception.SqsMessageException;
import co.com.crediya.sqs.listener.dto.ReportResponse;
import co.com.crediya.sqs.listener.mapper.SqsMapper;
import co.com.crediya.usecase.reportapplication.IReportApplicationUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

import static co.com.crediya.model.utils.Constant.ERROR_JSON_PROCESSING_DES;
import static co.com.crediya.model.utils.Constant.LOG_INFO_SQS_RECEIVED_REPORT;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ObjectMapper objectMapper;
    private final IReportApplicationUseCase reportApplicationUseCase;
    private final SqsMapper sqsMapper;

    @Override
    public Mono<Void> apply(Message message) {

        log.info(LOG_INFO_SQS_RECEIVED_REPORT, message.messageId());

        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), ReportResponse.class))
                .onErrorMap(JsonProcessingException.class,
                        e -> new SqsMessageException(ERROR_JSON_PROCESSING_DES))
                .flatMap(response ->
                        reportApplicationUseCase.updateReportLoan(sqsMapper.toNotificationData(response))
                );
    }
}
