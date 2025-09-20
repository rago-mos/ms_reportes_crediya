package co.com.crediya.sqs.listener.dto;

import java.math.BigDecimal;
import java.util.List;

public record ReportResponse(

        Integer idStatus,
        BigDecimal amount
) {
}
