package se.kry.codetest.migrate;

import io.vertx.core.Vertx;
import se.kry.codetest.DBConnector;

public class DBMigration {
  private static final String sql = "CREATE TABLE IF NOT EXISTS service (id INTEGER PRIMARY KEY, uri VARCHAR(128) NOT NULL, name VARCHAR(255), status VARCHAR(255), creation VARCHAR(255))";
 
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DBConnector connector = new DBConnector(vertx);

    connector.query(sql).setHandler(done -> {
      if (done.succeeded()) {
        System.out.println("completed db migrations");
      } else {
        System.out.println("error when applying db migrations");
        done.cause().printStackTrace();
      }

      connector.close((nothing) -> {
        vertx.close(shutdown -> {
          System.out.println("vert.x shutdown");
          System.exit(0);
        });
      });
    });
  }
}
