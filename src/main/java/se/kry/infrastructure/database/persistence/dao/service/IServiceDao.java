package se.kry.infrastructure.database.persistence.dao.service;

import io.vertx.core.Future;
import se.kry.domain.entity.Service;

import java.util.ArrayList;

public interface IServiceDao {
    Future<Service> create(Service service);

    Future<ArrayList<Service>> getAll();

    Future<Service> get(Integer id);

    Future<?> remove(Integer id);

    Future<Service> update(Service service);
}
