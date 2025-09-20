package co.com.crediya.dynamodb.entities;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ReportLoanEntityTest {

    @Test
    void shouldSetAndGetFieldsCorrectly() {

        ReportLoanEntity entity = new ReportLoanEntity();
        entity.setIdLoanReport("report-001");
        entity.setTotalLoan(10L);
        entity.setTotalAmount(new BigDecimal("5000"));

        assertThat(entity.getIdLoanReport()).isEqualTo("report-001");
        assertThat(entity.getTotalLoan()).isEqualTo(10L);
        assertThat(entity.getTotalAmount()).isEqualByComparingTo("5000");
    }

    @Test
    void shouldCreateEntityUsingSetters() {

        ReportLoanEntity entity = new ReportLoanEntity();
        entity.setIdLoanReport("report-002");
        entity.setTotalLoan(20L);
        entity.setTotalAmount(new BigDecimal("10000"));

        assertThat(entity.getIdLoanReport()).isEqualTo("report-002");
        assertThat(entity.getTotalLoan()).isEqualTo(20L);
        assertThat(entity.getTotalAmount()).isEqualByComparingTo("10000");
    }

    @Test
    void shouldHandleNullValuesGracefully() {

        ReportLoanEntity entity = new ReportLoanEntity();
        entity.setIdLoanReport(null);
        entity.setTotalLoan(null);
        entity.setTotalAmount(null);

        assertThat(entity.getIdLoanReport()).isNull();
        assertThat(entity.getTotalLoan()).isNull();
        assertThat(entity.getTotalAmount()).isNull();
    }

    @Test
    void shouldHaveDynamoDbAnnotations() throws Exception {
        Method idMethod = ReportLoanEntity.class.getMethod("getIdLoanReport");
        Method loanMethod = ReportLoanEntity.class.getMethod("getTotalLoan");
        Method amountMethod = ReportLoanEntity.class.getMethod("getTotalAmount");

        assertThat(idMethod.isAnnotationPresent(DynamoDbPartitionKey.class)).isTrue();
        assertThat(idMethod.getAnnotation(DynamoDbAttribute.class).value()).isEqualTo("id_loan_report");
        assertThat(loanMethod.getAnnotation(DynamoDbAttribute.class).value()).isEqualTo("total_loan");
        assertThat(amountMethod.getAnnotation(DynamoDbAttribute.class).value()).isEqualTo("total_amount");
    }

}
