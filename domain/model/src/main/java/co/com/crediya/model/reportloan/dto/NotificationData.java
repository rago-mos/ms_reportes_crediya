package co.com.crediya.model.reportloan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotificationData {

    private Integer idStatus;
    private BigDecimal amount;

}
