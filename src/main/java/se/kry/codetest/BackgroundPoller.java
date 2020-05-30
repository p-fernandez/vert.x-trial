package se.kry.codetest;

import io.vertx.core.Vertx;
import se.kry.domain.use_cases.services.CallMultipleServices;
import se.kry.domain.use_cases.services.GetAllServices;
import se.kry.domain.use_cases.services.UpdateMultipleServices;

public class BackgroundPoller {

  private CallMultipleServices callMultipleServices;
  private GetAllServices getAllServices;
  private UpdateMultipleServices updateMultipleServices;

  public BackgroundPoller(Vertx vertx) {
    callMultipleServices = new CallMultipleServices(vertx);
    getAllServices = new GetAllServices(vertx);
    updateMultipleServices = new UpdateMultipleServices(vertx);
  }

  public void pollServices() {
    getAllServices.execute()
        .compose(services -> callMultipleServices.execute(services))
        .compose(services -> updateMultipleServices.execute(services));
  }
}
