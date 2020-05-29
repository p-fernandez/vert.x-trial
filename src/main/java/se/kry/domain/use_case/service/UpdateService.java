package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import se.kry.domain.entity.Service;
import se.kry.domain.entity.ServiceManagerImpl;

public class UpdateService {
    public ServiceManagerImpl serviceManagerImpl;
    public Service service;

    public UpdateService(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<Service> execute(String id, JsonObject jsonBody) {
        Future<Service> updateService = Future.future();

        jsonBody.put("id", Integer.valueOf(id));
        service = new Service(jsonBody);

        serviceManagerImpl.updateService(service).setHandler(res -> {
            if (res.failed()) {
                updateService.fail(res.cause());
            } else {
                updateService.complete(res.result());
            }
        });

        return updateService;
    }
}
