package co.com.crediya.sqs.listener.mapper;

import co.com.crediya.model.reportloan.dto.NotificationData;
import co.com.crediya.sqs.listener.dto.ReportResponse;
import org.springframework.stereotype.Component;

@Component
public class SqsMapper {

    public NotificationData toNotificationData(ReportResponse response) {
        return NotificationData.builder()
                .idStatus(response.idStatus())
                .amount(response.amount())
                .build();
    }
}
