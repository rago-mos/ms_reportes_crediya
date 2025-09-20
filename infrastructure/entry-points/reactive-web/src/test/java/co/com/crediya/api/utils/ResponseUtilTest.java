package co.com.crediya.api.utils;

import co.com.crediya.api.dto.response.GenericResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseUtilTest {

    @Test
    void shouldBuildSuccessfulResponseWithData() {

        String payload = "mock-payload";

        GenericResponse<String> response = ResponseUtil.responseSuccessful(payload);

        assertThat(response).isNotNull();
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("CODE_001");
        assertThat(response.getMessage()).isEqualTo("Report loan created successfully");
        assertThat(response.getData()).isEqualTo(payload);
    }

    @Test
    void shouldBuildSuccessfulResponseWithNullData() {

        GenericResponse<Object> response = ResponseUtil.responseSuccessful(null);

        assertThat(response).isNotNull();
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("CODE_001");
        assertThat(response.getMessage()).isEqualTo("Report loan created successfully");
        assertThat(response.getData()).isNull();
    }

    @Test
    void shouldGenerateRecentTimestamp() {

        GenericResponse<String> response = ResponseUtil.responseSuccessful("data");

        assertThat(response.getTimestamp())
                .isBefore(LocalDateTime.now().plusSeconds(1))
                .isAfter(LocalDateTime.now().minusSeconds(2));
    }
}