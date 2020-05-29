package se.kry.infrastructure.database.persistence.dao.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import se.kry.domain.entity.Service;
import se.kry.infrastructure.interfaces.exception.DatabaseRecordNotFoundException;
import se.kry.infrastructure.database.persistence.dao.DaoImpl;

import java.util.ArrayList;
import java.util.List;

public class ServiceDaoImpl extends DaoImpl implements IServiceDao {
    private static final String DB_PATH = "poller.db";
    private static final String DB_NAME = "service";

    public ServiceDaoImpl(Vertx vertx) {
        super(vertx, DB_PATH);
    }

    public Future<Service> create(Service service) {
        String sql = "INSERT INTO " + DB_NAME + " (url, name, status, creation) VALUES (?, ?, ?, ?)";

        Future<Service> createdService = Future.future();

        JsonArray params = new JsonArray();
        params.add(service.getUrl());
        params.add(service.getName());
        params.add(service.getStatus());
        params.add(service.getCreation());

        super.create(sql, params).setHandler(res -> {
            if (res.failed()) {
                createdService.fail(res.cause());
            } else {
                service.setId(res.result().getKeys().getInteger(0));
                createdService.complete(service);
            }
        });
        return createdService;
    }

    public Future<ArrayList<Service>> getAll() {
        String sql = "SELECT * FROM " + DB_NAME;

        Future<ArrayList<Service>> services = Future.future();

        super.getAll(sql).setHandler(res -> {
            if (res.failed()) {
                services.fail(res.cause());
            } else {
                try {
                    ArrayList<Service> servicesFromJson = new ArrayList<>();
                    ArrayList<?> jsonList = (ArrayList<?>) res.result();
                    jsonList.forEach(json -> {
                        Service service = new Service((JsonObject) json);
                        servicesFromJson.add(service);
                    });

                    services.complete(servicesFromJson);
                } catch (Exception ex) {
                    services.fail(ex);
                }
            }
        });
        return services;
    }

    public Future<Service> get(Integer id) {
        String sql = "SELECT * FROM " + DB_NAME + " WHERE id = ?";

        Future<Service> service = Future.future();

        JsonArray params = new JsonArray();
        params.add(id);

        super.get(sql, params).setHandler(res -> {
            if (res.failed()) {
                service.fail(res.cause());
            } else {
                List<JsonObject> rows = res.result().getRows();
                // rows.get(999999999); // Uncomment / comment to force exception or not

                if (rows.size() == 0) {
                    service.fail(new DatabaseRecordNotFoundException("Id : " + id + " not found"));
                } else {
                    JsonObject json = rows.get(0);
                    Service storedService = new Service(json);
                    service.complete(storedService);
                }
            }
        });

        return service;
    }

    public Future<?> remove(Integer id) {
        String sql = "DELETE FROM " + DB_NAME + " WHERE id = ?";

        Future<?> removedService = Future.future();

        JsonArray params = new JsonArray();
        params.add(id);

        super.get(sql, params).setHandler(res -> {
            if (res.failed()) {
                removedService.fail(res.cause());
            } else {
                removedService.complete();
            }
        });
        return removedService;
    }

    public Future<Service> update(Service service) {
        String sql = "UPDATE " + DB_NAME + " SET url = ?, name = ?, status = ? WHERE id = ?";

        Future<Service> updatedService = Future.future();

        JsonArray params = new JsonArray();
        params.add(service.getUrl());
        params.add(service.getName());
        params.add(service.getStatus());
        params.add(service.getId());

        super.update(sql, params).setHandler(res -> {
            if (res.failed()) {
                updatedService.fail(res.cause());
            } else {
                updatedService.complete(service);
            }
        });
        return updatedService;
    }
}
