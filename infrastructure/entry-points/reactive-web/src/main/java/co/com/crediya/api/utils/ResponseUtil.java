package co.com.crediya.api.utils;

import co.com.crediya.api.dto.response.GenericResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static co.com.crediya.model.utils.Constant.*;

@UtilityClass
public class ResponseUtil {

    public static <T> GenericResponse<T> responseSuccessful(T data) {
        return GenericResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(STATUS_OK)
                .message(MESSAGE_REPORT_SUCCEFULLY)
                .data(data)
                .build();
    }
}

