package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.applications.validators.RequestParamValidator;
import se.kry.domain.entities.Service;
import se.kry.domain.entities.ServiceManagerImpl;

public class RemoveService {
    public RequestParamValidator requestParamValidator;
    public ServiceManagerImpl serviceManagerImpl;

    public RemoveService(Vertx vertx) {
        requestParamValidator = new RequestParamValidator();
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<?> execute(String id) {
        Future<Service> removedService = Future.future();

        try {
            requestParamValidator.validateId(id);

            serviceManagerImpl.removeService(Integer.valueOf(id)).setHandler(res -> {
                if (res.failed()) {
                    removedService.fail(res.cause());
                } else {
                    removedService.complete();
                }
            });
        } catch (Exception e) {
            removedService.fail(e);
        }

        return removedService;
    }
}