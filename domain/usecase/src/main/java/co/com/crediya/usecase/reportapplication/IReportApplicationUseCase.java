package co.com.crediya.usecase.reportapplication;

import co.com.crediya.model.reportloan.ReportLoan;
import co.com.crediya.model.reportloan.dto.NotificationData;
import reactor.core.publisher.Mono;

public interface IReportApplicationUseCase {

    Mono<ReportLoan> getReportLoan();
    Mono<Void> updateReportLoan(NotificationData data);
}
