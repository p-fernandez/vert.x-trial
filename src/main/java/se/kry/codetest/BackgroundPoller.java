package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class BackgroundPoller {

  public BackgroundPoller(Vertx vertx) {
  }

  public Future<?> pollServices() {
    Future<?> poller = Future.future();

    return poller;
  }


}
