package co.com.crediya.model.reportloan;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportLoan {

    private Long totalLoan;
    private BigDecimal totalAmount;
}
