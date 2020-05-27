package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entity.Service;
import se.kry.domain.entity.ServiceManagerImpl;

public class RemoveService {
    public ServiceManagerImpl serviceManagerImpl;

    public RemoveService(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<?> execute(String id) {
        Future<Service> service = Future.future();

        serviceManagerImpl.removeService(Integer.valueOf(id)).setHandler(res -> {
            if (res.failed()) {
                service.fail(res.cause());
            } else {
                service.complete();
            }
        });

        return service;
    }
}