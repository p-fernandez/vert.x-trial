package se.kry.infrastructure.database.persistence.clients;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.UpdateResult;

public class SQLiteClient {
  private final static String driverClass = "org.sqlite.JDBC";
  private final static int maxPoolSize = 30;

  public final SQLClient client;

  public SQLiteClient(Vertx vertx, String databasePath){
    JsonObject config = new JsonObject()
        .put("url", "jdbc:sqlite:" + databasePath)
        .put("driver_class", driverClass)
        .put("max_pool_size", maxPoolSize);

    client = JDBCClient.createShared(vertx, config);
  }

  public void close(Handler<AsyncResult<Void>> next) {
    client.close(closeHandler -> {
      if (closeHandler.succeeded()) {
        System.out.println("Database Connection closed");
        next.handle(Future.succeededFuture());
      } else if (closeHandler.failed()) {
        System.out.println("Database Connection failed to close!");
      } else {
        System.out.println("Debug flow because it shouldn't arrive here.");
      }
    });
  }

  public Future<ResultSet> query(String query) {
    return query(query, new JsonArray());
  }

  public Future<ResultSet> query(String query, JsonArray params) {
    if(query == null || query.isEmpty()) {
      return Future.failedFuture("Query is null or empty");
    }
    if(!query.endsWith(";")) {
      query = query + ";";
    }

    Future<ResultSet> queryResultFuture = Future.future();

    client.queryWithParams(query, params, result -> {
      if(result.failed()){
        queryResultFuture.fail(result.cause());
      } else {
        queryResultFuture.complete(result.result());
      }
    });
    return queryResultFuture;
  }

  public Future<UpdateResult> update(String query) {
    return update(query, new JsonArray());
  }

  public Future<UpdateResult> update(String query, JsonArray params) {
    if(query == null || query.isEmpty()) {
      return Future.failedFuture("Query is null or empty");
    }
    if(!query.endsWith(";")) {
      query = query + ";";
    }

    Future<UpdateResult> queryResultFuture = Future.future();

    client.updateWithParams(query, params, result -> {
      if(result.failed()){
        queryResultFuture.fail(result.cause());
      } else {
        queryResultFuture.complete(result.result());
      }
    });
    return queryResultFuture;
  }
}
