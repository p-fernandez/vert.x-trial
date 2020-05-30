package se.kry.domain.entities;

import io.vertx.core.Future;

import java.util.List;

public interface IServiceManager {
  Future<Service> createService(Service service);

  Future<List<Service>> getAllServices();

  Future<Service> getService(Integer id);

  Future<?> removeService(Integer id);
}
