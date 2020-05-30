package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import se.kry.domain.applications.validators.RequestBodyValidator;
import se.kry.domain.applications.validators.RequestParamValidator;
import se.kry.domain.entities.Service;
import se.kry.domain.entities.ServiceManagerImpl;

public class UpdateService {
    public ServiceManagerImpl serviceManagerImpl;
    public Service service;
    public RequestBodyValidator requestBodyValidator;
    public RequestParamValidator requestParamValidator;

    public UpdateService(Vertx vertx) {
        requestBodyValidator = new RequestBodyValidator();
        requestParamValidator = new RequestParamValidator();
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(String id, JsonObject jsonBody) {
        Future<Service> updateService = Future.future();

        try {
            requestParamValidator.validateId(id);
            requestBodyValidator.validateBody(jsonBody);

            jsonBody.put("id", Integer.valueOf(id));
            service = new Service(jsonBody);

            serviceManagerImpl.updateService(service).setHandler(res -> {
                if (res.failed()) {
                    updateService.fail(res.cause());
                } else {
                    updateService.complete(res.result());
                }
            });
        } catch (Exception e) {
            updateService.fail(e);
        }

        return updateService;
    }
}
