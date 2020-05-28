package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import se.kry.domain.entity.Service;
import se.kry.domain.entity.ServiceManagerImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateService {
    public ServiceManagerImpl serviceManagerImpl;
    public Service service;

    public CreateService(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(JsonObject jsonBody) {
        Future<Service> createdService = Future.future();

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
