package co.com.crediya.model.reportloan.gateways;

import co.com.crediya.model.reportloan.ReportLoan;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ReportLoanRepository {

    Mono<ReportLoan> getReport();
    Mono<Void> updateReport(BigDecimal amountAdd);
}
