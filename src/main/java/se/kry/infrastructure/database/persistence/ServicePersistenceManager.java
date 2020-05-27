package se.kry.infrastructure.database.persistence;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entity.Service;
import se.kry.infrastructure.database.persistence.dao.service.ServiceDaoImpl;

import java.util.List;

public class ServicePersistenceManager {
  private final ServiceDaoImpl serviceDao;

  public ServicePersistenceManager(Vertx vertx) {
    serviceDao = new ServiceDaoImpl(vertx);
  }

  public Future<Service> createService(Service service) {
    Future<Service> createdService = Future.future();

    serviceDao.create(service).setHandler(res -> {
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

    serviceDao.getAll().setHandler(res -> {
      if (res.failed()) {
        services.fail(res.cause());
      } else {
        services.complete(res.result());
      }
    });

    return services;
  }
}
