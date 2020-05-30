package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.applications.validators.RequestParamValidator;
import se.kry.domain.entities.Service;
import se.kry.domain.entities.ServiceManagerImpl;

public class GetService {
    public ServiceManagerImpl serviceManagerImpl;
    public RequestParamValidator requestParamValidator;

    public GetService(Vertx vertx) {
        requestParamValidator = new RequestParamValidator();
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(String id) {
        Future<Service> gotService = Future.future();

        try {
            requestParamValidator.validateId(id);

            serviceManagerImpl.getService(Integer.valueOf(id)).setHandler(res -> {
                if (res.failed()) {
                    gotService.fail(res.cause());
                } else {
                    gotService.complete(res.result());
                }
            });
        } catch (Exception e) {
            gotService.fail(e);
        }

        return gotService;
    }
}