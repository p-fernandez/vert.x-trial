package se.kry.infrastructure.database.persistence.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;

public interface IDao {
    Future<UpdateResult> create(String sql, JsonArray params);

    Future<List<?>> getAll(String sql);

    Future<ResultSet> get(String sql, JsonArray params);

    Future<ResultSet> remove(String sql, JsonArray params);

    Future<UpdateResult> update(String sql, JsonArray params);

}
