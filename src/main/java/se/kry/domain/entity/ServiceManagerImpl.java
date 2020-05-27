package se.kry.domain.entity;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.infrastructure.database.persistence.ServicePersistenceManager;

import java.util.List;

public class ServiceManagerImpl implements ServiceManager {
    private final ServicePersistenceManager persistenceManager;

   public ServiceManagerImpl(Vertx vertx) {
       persistenceManager = new ServicePersistenceManager(vertx);
   }

    public Future<Service> createService(Service service) {
        Future<Service> createdService = Future.future();

        persistenceManager.createService(service).setHandler(res -> {
            if (res.failed()) {
                createdService.fail(res.cause());
            } else {
                createdService.complete(res.result());
            }
        });

        return createdService;
    }

    public Future<List<Service>> getAllServices() {
        Future<List<Service>> services = Future.future();

        persistenceManager.getAllServices().setHandler(res -> {
            if (res.failed()) {
                services.fail(res.cause());
            } else {
                services.complete(res.result());
            }
        });

        return services;
    }

    public Future<Service> getService(Integer id) {
        Future<Service> service = Future.future();

        persistenceManager.getService(id).setHandler(res -> {
            if (res.failed()) {
                service.fail(res.cause());
            } else {
                service.complete(res.result());
            }
        });

        return service;
    }

    public Future<?> removeService(Integer id) {
        Future<?> removedService = Future.future();

        persistenceManager.removeService(id).setHandler(res -> {
            if (res.failed()) {
                removedService.fail(res.cause());
            } else {
                removedService.complete();
            }
        });

        return removedService;
    }

}
