package co.com.crediya.sqs.listener;

import co.com.crediya.model.exception.SqsMessageException;
import co.com.crediya.model.reportloan.dto.NotificationData;
import co.com.crediya.sqs.listener.dto.ReportResponse;
import co.com.crediya.sqs.listener.mapper.SqsMapper;
import co.com.crediya.usecase.reportapplication.IReportApplicationUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.model.Message;

import java.math.BigDecimal;

import static co.com.crediya.model.utils.Constant.ERROR_JSON_PROCESSING_DES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSProcessorTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private IReportApplicationUseCase reportApplicationUseCase;

    @Mock
    private SqsMapper sqsMapper;

    @InjectMocks
    private SQSProcessor sqsProcessor;

    @Test
    void shouldProcessMessageSuccessfully() throws Exception {

        String messageBody = "{\"idStatus\":1,\"amount\":100.0}";
        Message message = Message.builder().body(messageBody).messageId("msg-123").build();

        ReportResponse reportResponse = new ReportResponse(1, new BigDecimal("100.0"));
        NotificationData notificationData = NotificationData.builder()
                .idStatus(1)
                .amount(new BigDecimal("100.0"))
                .build();

        when(objectMapper.readValue(messageBody, ReportResponse.class)).thenReturn(reportResponse);
        when(sqsMapper.toNotificationData(reportResponse)).thenReturn(notificationData);
        when(reportApplicationUseCase.updateReportLoan(notificationData)).thenReturn(Mono.empty());

        Mono<Void> result = sqsProcessor.apply(message);

        StepVerifier.create(result).verifyComplete();
        verify(objectMapper).readValue(messageBody, ReportResponse.class);
        verify(sqsMapper).toNotificationData(reportResponse);
        verify(reportApplicationUseCase).updateReportLoan(notificationData);
    }

    @Test
    void shouldThrowSqsMessageExceptionOnJsonError() throws Exception {

        String invalidBody = "invalid-json";
        Message message = Message.builder().body(invalidBody).messageId("msg-456").build();

        when(objectMapper.readValue(invalidBody, ReportResponse.class))
                .thenThrow(new JsonProcessingException("error") {});

        Mono<Void> result = sqsProcessor.apply(message);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(SqsMessageException.class);
                    assertThat(error.getMessage()).isEqualTo(ERROR_JSON_PROCESSING_DES);
                })
                .verify();

        verify(objectMapper).readValue(invalidBody, ReportResponse.class);
        verifyNoInteractions(reportApplicationUseCase);
    }
}