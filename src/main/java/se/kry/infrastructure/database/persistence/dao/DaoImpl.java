package se.kry.infrastructure.database.persistence.dao;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import se.kry.infrastructure.database.persistence.client.SQLiteClient;

import java.util.List;

public abstract class DaoImpl implements IDao{
    private final SQLiteClient client;

    public DaoImpl(Vertx vertx, String databasePath) {
        client = new SQLiteClient(vertx, databasePath);
    }

    public Future<UpdateResult> create(String sql, JsonArray params) {
        Future<UpdateResult> queryResultFuture = Future.future();

        client.update(sql, params).setHandler(res -> {
            if (res.failed()) {
                queryResultFuture.fail(res.cause());
            } else {
                queryResultFuture.complete(res.result());
            }
        });
        return queryResultFuture;
    }

    public Future<List<?>> getAll(String sql) {
        Future<List<?>> queryResultFuture = Future.future();

        client.query(sql).setHandler(res -> {
            if (res.failed()) {
                queryResultFuture.fail(res.cause());
            } else {
                List<?> results = res.result().getRows();
                queryResultFuture.complete(results);
            }
        });
        return queryResultFuture;
    }

    public Future<ResultSet> get(String sql, JsonArray params) {
        Future<ResultSet> queryResultFuture = Future.future();

        client.query(sql, params).setHandler(res -> {
            if (res.failed()) {
                queryResultFuture.fail(res.cause());
            } else {
                queryResultFuture.complete(res.result());
            }
        });
        return queryResultFuture;
    }

    public Future<ResultSet> remove(String sql, JsonArray params) {
        Future<ResultSet> queryResultFuture = Future.future();

        client.query(sql, params).setHandler(res -> {
            if (res.failed()) {
                queryResultFuture.fail(res.cause());
            } else {
                queryResultFuture.complete(res.result());
            }
        });
        return queryResultFuture;
    }
}