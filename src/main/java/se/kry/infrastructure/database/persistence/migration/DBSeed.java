package se.kry.infrastructure.database.persistence.migration;

import io.vertx.core.Vertx;
import se.kry.domain.config.services.Database;
import se.kry.infrastructure.database.persistence.clients.SQLiteClient;

import javax.xml.crypto.Data;

public class DBSeed {
  private static final String resetDatabaseSql = "DELETE FROM " + Database.TABLE;
  private static final String seedDatabaseSql = "INSERT INTO " + Database.TABLE + " (id, url, name, status, creation) SELECT 1, 'https://www.google.com', 'Google', 'UNKNOWN', '1590793200000' WHERE NOT EXISTS (SELECT 1 FROM service WHERE id=1 AND url='https://www.google.com' AND name='Google' AND status='UNKNOWN' AND creation='1590793200000')";
  private static final String DB_PATH = Database.NAME;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    SQLiteClient connector = new SQLiteClient(vertx, DB_PATH);

    connector.query(resetDatabaseSql)
      .compose(res -> {
        System.out.println("completed db wipe");

        return connector.update(seedDatabaseSql);
      })
      .setHandler(done -> {
        if (done.succeeded()) {
          System.out.println("completed db seed");
        } else {
          System.out.println("error when applying db seed");
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
