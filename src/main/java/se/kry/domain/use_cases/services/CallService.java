package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.config.services.ServiceStatus;
import se.kry.domain.entities.Service;
import se.kry.repository.web.HttpClient;

public class CallService {
    private static final Integer timeout = 5000;
    private final HttpClient httpClient;

    public CallService(Vertx vertx) {
        httpClient = new HttpClient(vertx);
    }

    public Future<Service> execute(Service service) {
        Future<Service> calledService = Future.future();

        try {
            httpClient.get(service.getUrl(), timeout)
                    .setHandler(res -> {
                        if (res.failed()) {
                            service.setStatus(String.valueOf(ServiceStatus.FAIL));
                        } else {
                            String status = String.valueOf(res.result().getStatusCode() == 200 ? ServiceStatus.OK : ServiceStatus.FAIL);
                            service.setStatus(status);
                        }

                        calledService.complete(service);
                    });
        } catch (Throwable e) {
            calledService.complete(service);
        }

        return calledService;
    }
}
