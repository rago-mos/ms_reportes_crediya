package co.com.crediya.dynamodb.entities;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;

@DynamoDbBean
public class ReportLoanEntity {

    private String idLoanReport;
    private Long totalLoan;
    private BigDecimal totalAmount;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id_loan_report")
    public String getIdLoanReport() {
        return idLoanReport;
    }

    public void setIdLoanReport(String idLoanReport) {
        this.idLoanReport = idLoanReport;
    }

    @DynamoDbAttribute("total_loan")
    public Long getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(Long totalLoan) {
        this.totalLoan = totalLoan;
    }

    @DynamoDbAttribute("total_amount")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
