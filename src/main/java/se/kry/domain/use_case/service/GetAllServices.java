package se.kry.domain.use_case.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entity.Service;
import se.kry.domain.entity.ServiceManagerImpl;

import java.util.List;

public class GetAllServices {
    public ServiceManagerImpl serviceManagerImpl;

    public GetAllServices(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<List<Service>> execute() {
        Future<List<Service>> services = Future.future();

        serviceManagerImpl.getAllServices().setHandler(res -> {
            if (res.failed()) {
                services.fail(res.cause());
            } else {
                services.complete(res.result());
            }
        });

        return services;
    }
}