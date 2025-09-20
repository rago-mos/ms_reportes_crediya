package co.com.crediya.api;

import co.com.crediya.usecase.reportapplication.IReportApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.crediya.api.utils.ResponseUtil.responseSuccessful;
import static co.com.crediya.model.utils.Constant.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final IReportApplicationUseCase reportApplicationUseCase;

    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ServerResponse> listenGETUseCase(ServerRequest request) {

        return reportApplicationUseCase.getReportLoan()
                .flatMap(response -> {
                        log.info(LOG_INFO_REPORT);
                        var body = responseSuccessful(response);
                        return ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body);
                });
    }

}
