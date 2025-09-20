package co.com.crediya.sqs.listener.mapper;

import co.com.crediya.model.reportloan.dto.NotificationData;
import co.com.crediya.sqs.listener.dto.ReportResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SqsMapperTest {

    private SqsMapper sqsMapper;

    @BeforeEach
    void setUp() {
        sqsMapper = new SqsMapper();
    }

    @Test
    void shouldMapReportResponseToNotificationDataCorrectly() {

        ReportResponse response = new ReportResponse(1, new BigDecimal("150.00"));

        NotificationData result = sqsMapper.toNotificationData(response);

        assertThat(result).isNotNull();
        assertThat(result.getIdStatus()).isEqualTo(1);
        assertThat(result.getAmount()).isEqualByComparingTo("150.00");
    }

    @Test
    void shouldHandleNullValuesGracefully() {

        ReportResponse response = new ReportResponse(null, null);

        NotificationData result = sqsMapper.toNotificationData(response);

        assertThat(result).isNotNull();
        assertThat(result.getIdStatus()).isNull();
        assertThat(result.getAmount()).isNull();
    }
}