package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import se.kry.domain.config.websockets.WebSocket;
import se.kry.domain.interfaces.adapters.WebSocketAdapter;
import se.kry.domain.use_cases.services.CallMultipleServices;
import se.kry.domain.use_cases.services.GetAllServices;
import se.kry.domain.use_cases.services.UpdateMultipleServices;

public class BackgroundPoller {

  private final CallMultipleServices callMultipleServices;
  private final GetAllServices getAllServices;
  private final EventBus eventBus;
  private final UpdateMultipleServices updateMultipleServices;

  public BackgroundPoller(Vertx vertx) {
    callMultipleServices = new CallMultipleServices(vertx);
    eventBus = vertx.eventBus();
    getAllServices = new GetAllServices(vertx);
    updateMultipleServices = new UpdateMultipleServices(vertx);
  }

  public void pollServices() {
    getAllServices.execute()
        .compose(callMultipleServices::execute)
        .compose(updateMultipleServices::execute)
        .compose(services -> Future.succeededFuture(eventBus.publish(WebSocket.WS_PATH, WebSocketAdapter.buildMessageFromServices(services))));
  }
}
