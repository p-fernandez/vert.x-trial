package se.kry.domain.use_cases.services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entities.Service;
import se.kry.domain.entities.ServiceManagerImpl;

import java.util.List;

public class GetAllServices {
    public ServiceManagerImpl serviceManagerImpl;

    public GetAllServices(Vertx vertx) {
        serviceManagerImpl = new ServiceManagerImpl(vertx);
    }

    public Future<List<Service>> execute() {
        Future<List<Service>> gotServices = Future.future();

        serviceManagerImpl.getAllServices().setHandler(res -> {
            if (res.failed()) {
                gotServices.fail(res.cause());
            } else {
                gotServices.complete(res.result());
            }
        });

        return gotServices;
    }
}