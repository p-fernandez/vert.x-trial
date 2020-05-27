package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import se.kry.domain.entity.Service;
import se.kry.domain.entity.ServiceManagerImpl;

public class CreateService {
    public ServiceManagerImpl serviceManagerImpl;
    public Service service;

    public CreateService(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(JsonObject jsonBody) {
        Future<Service> createdService = Future.future();

        jsonBody.put("id", 0);
        jsonBody.put("creation", "2020-12-21");

        service = new Service(jsonBody);

        serviceManagerImpl.createService(service).setHandler(res -> {
            if (res.failed()) {
                createdService.fail(res.cause());
            } else {
                createdService.complete(res.result());
            }
        });

        return createdService;
    }
}
