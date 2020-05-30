package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import se.kry.domain.applications.validators.RequestBodyValidator;
import se.kry.domain.entities.Service;
import se.kry.domain.entities.ServiceManagerImpl;


public class CreateService {
    public ServiceManagerImpl serviceManagerImpl;
    public Service service;
    public RequestBodyValidator requestBodyValidator;

    public CreateService(Vertx vertx) {
        requestBodyValidator = new RequestBodyValidator();
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(JsonObject jsonBody) {
        Future<Service> createdService = Future.future();
        try {
            requestBodyValidator.validateBody(jsonBody);

            service = new Service(jsonBody);

            serviceManagerImpl.createService(service).setHandler(res -> {
                if (res.failed()) {
                    createdService.fail(res.cause());
                } else {
                    createdService.complete(res.result());
                }
            });
        } catch (Exception e) {
            createdService.fail(e);
        }

        return createdService;
    }
}
