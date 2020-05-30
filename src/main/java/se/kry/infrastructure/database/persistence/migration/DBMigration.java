package se.kry.infrastructure.database.persistence.migration;

import io.vertx.core.Vertx;
import se.kry.domain.config.services.Database;
import se.kry.infrastructure.database.persistence.clients.SQLiteClient;

public class DBMigration {
  private static final String sql = "CREATE TABLE IF NOT EXISTS " + Database.TABLE + " (id INTEGER PRIMARY KEY, url VARCHAR(128) NOT NULL, name VARCHAR(255), status VARCHAR(255), creation VARCHAR(255))";
  private static final String DB_PATH = Database.NAME;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    SQLiteClient connector = new SQLiteClient(vertx, DB_PATH);

    connector.query(sql).setHandler(done -> {
      if (done.succeeded()) {
        System.out.println("completed db migrations");
      } else {
        System.out.println("error when applying db migrations");
        done.cause().printStackTrace();
      }

      connector.close((nothing) -> vertx.close(shutdown -> {
          System.out.println("vert.x shutdown");
          System.exit(0);
        })
      );
    });
  }
}
