package se.kry.infrastructure.database.persistence;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import se.kry.domain.entities.Service;
import se.kry.infrastructure.database.persistence.dao.services.ServiceDaoImpl;

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

  public Future<Service> getService(Integer id) {
      Future<Service> service = Future.future();

      serviceDao.get(id).setHandler(res -> {
        if (res.failed()) {
          service.fail(res.cause());
        } else {
          service.complete(res.result());
        }
      });

      return service;
  }

  public Future<?> removeService(Integer id) {
    Future<Service> service = Future.future();

    serviceDao.get(id).compose(outcome -> serviceDao.remove(id)).setHandler(res -> {
      if (res.failed()) {
        service.fail(res.cause());
      } else {
        service.complete();
      }
    });

    return service;
  }

  public Future<Service> updateService(Service service) {
    Future<Service> updatedService = Future.future();

    serviceDao.get(service.getId()).compose(outcome -> {
      Service preparedService = prepareServiceToUpdate(service, outcome);
      return serviceDao.update(preparedService);
    }).setHandler(res -> {
      if (res.failed()) {
        updatedService.fail(res.cause());
      } else {
        updatedService.complete(res.result());
      }
    });

    return updatedService;
  }

  public Service prepareServiceToUpdate(Service service, Service dbService) {
    if (service.getUrl() == null) {
      service.setUrl(dbService.getUrl());
    }
    if (service.getName() == null || service.getName().equals("")) {
      service.setName(dbService.getName());
    }
    if (service.getStatus() == null) {
      service.setName(dbService.getStatus());
    }

    service.setCreation(dbService.getCreation());

    return service;
  }
}
