package se.kry.domain.entity;

import io.vertx.core.Future;

import java.util.List;

public interface ServiceManager {
  Future<Service> createService(Service service);

  Future<List<Service>> getAllServices();
}
