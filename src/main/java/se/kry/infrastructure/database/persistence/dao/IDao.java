package se.kry.infrastructure.database.persistence.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.List;

public interface IDao {
    Future<?> create(String sql, JsonArray params);

    Future<List<?>> getAll(String sql);

}
