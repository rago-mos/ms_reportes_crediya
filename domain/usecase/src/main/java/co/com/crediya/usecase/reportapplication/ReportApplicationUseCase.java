package co.com.crediya.usecase.reportapplication;

import co.com.crediya.model.reportloan.ReportLoan;
import co.com.crediya.model.reportloan.dto.NotificationData;
import co.com.crediya.model.reportloan.gateways.ReportLoanRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class ReportApplicationUseCase implements IReportApplicationUseCase {

    private final ReportLoanRepository reportLoanRepository;

    @Override
    public Mono<ReportLoan> getReportLoan() {
        return reportLoanRepository.getReport();
    }

    @Override
    public Mono<Void> updateReportLoan(NotificationData data) {
        return reportLoanRepository.updateReport(data.getAmount());
    }
}
