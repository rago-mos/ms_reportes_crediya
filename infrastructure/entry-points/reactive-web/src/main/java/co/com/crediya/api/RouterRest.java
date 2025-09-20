package co.com.crediya.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private static final String URL_REPORTES = "/api/v1/reportes";

    @Bean
    @RouterOperation(method = RequestMethod.GET,
            path = URL_REPORTES,
            beanClass = Handler.class,
            beanMethod = "listenGETUseCase"
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(URL_REPORTES), handler::listenGETUseCase);
    }
}
